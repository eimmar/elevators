package com.tingco.codechallenge.elevator.model

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.api.Elevator.Direction
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@ExperimentalCoroutinesApi
class BaseElevator(
        private val _id: Int,
        private val elevatorFloorTravelDurationMs: Long,
        private val elevatorScope: CoroutineScope,
) : Elevator {
    sealed class Command {
        data class MoveToFloor(val floorNumber: Int) : Command()
    }

    sealed class OutputMessage {
        data class IsAtFloor(val floorNumber: Int, val elevatorId: Int) : OutputMessage()
        data class StoppedAtFloor(val floorNumber: Int, val elevatorId: Int) : OutputMessage()
    }

    private var currentFloor: Int = 1
    private var addressedFloor: Int = 1
    private var direction: Direction = Direction.NONE
    private var additionalStops: MutableList<Int> = mutableListOf()

    private val commandChannel = Channel<Command>(10000)
    private val _outputChannel = BroadcastChannel<OutputMessage>(10000)

    init {
        elevatorScope.launch {
            while (isActive) {
                when (val command = commandChannel.receive()) {
                    is Command.MoveToFloor -> {
                        direction = if (currentFloor > command.floorNumber) Direction.DOWN else Direction.UP
                        addressedFloor = command.floorNumber

                        _outputChannel.send(OutputMessage.IsAtFloor(currentFloor, id))
                        while (command.floorNumber != currentFloor) {
                            when (direction) {
                                Direction.UP -> {
                                    delay(elevatorFloorTravelDurationMs)
                                    currentFloor++
                                    _outputChannel.send(OutputMessage.IsAtFloor(currentFloor, id))
                                    stopIfNeeded()
                                }

                                Direction.DOWN -> {
                                    delay(elevatorFloorTravelDurationMs)
                                    currentFloor--
                                    _outputChannel.send(OutputMessage.IsAtFloor(currentFloor, id))
                                    stopIfNeeded()
                                }
                            }
                        }

                        direction = Direction.NONE
                        _outputChannel.send(OutputMessage.StoppedAtFloor(currentFloor, id))
                        delay(elevatorFloorTravelDurationMs)

                        if (additionalStops.size != 0) {
                            commandChannel.send(Command.MoveToFloor(additionalStops.removeFirst()))
                        }
                    }
                }
            }
        }
    }

    private suspend fun stopIfNeeded() {
        var floorToStop: Int? = null
        if (direction == Direction.UP) {
            floorToStop = additionalStops.filter { it >= currentFloor }.minByOrNull { it }
        } else if (direction == Direction.DOWN) {
            floorToStop = additionalStops.filter { it <= currentFloor }.maxByOrNull { it }
        }

        if (floorToStop == currentFloor) {
            _outputChannel.send(OutputMessage.StoppedAtFloor(currentFloor, id))
            additionalStops.remove(floorToStop)
            delay(elevatorFloorTravelDurationMs)
        }
    }

    override fun getDirection(): Direction = direction
    override fun getAddressedFloor(): Int = addressedFloor
    override fun isBusy(): Boolean = direction != Direction.NONE
    override fun getCurrentFloor(): Int = currentFloor
    override val id get() = this._id
    val outputChannel get() = _outputChannel.openSubscription()

    fun addAdditionalStop(request: ElevatorRequest) {
        additionalStops = additionalStops
                .plus(listOf(request.requestedFromFloor, request.toFloor))
                .distinct()
                .toMutableList()
    }

    fun moveElevator(request: ElevatorRequest) {
        if (currentFloor == request.requestedFromFloor) {
            moveElevator(request.toFloor)
        } else {
            moveElevator(request.requestedFromFloor)
            moveElevator(request.toFloor)
        }
    }

    override fun moveElevator(toFloor: Int) {
        elevatorScope.launch {
            commandChannel.send(Command.MoveToFloor(toFloor))
        }
    }
}
