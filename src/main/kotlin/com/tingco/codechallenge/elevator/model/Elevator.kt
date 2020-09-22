package com.tingco.codechallenge.elevator.model

import Direction
import ElevatorRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@ExperimentalCoroutinesApi
class Elevator(
        val id: Int,
        private val elevatorFloorTravelDurationMs: Long,
        private val elevatorScope: CoroutineScope,
) {
    sealed class Command {
        data class MoveToFloor(val floorNumber: Int) : Command()
    }

    sealed class OutputMessage {
        data class IsAtFloor(val floorNumber: Int, val elevatorId: Int) : OutputMessage()
        data class StoppedAtFloor(val floorNumber: Int, val elevatorId: Int) : OutputMessage()
    }

    private val commandChannel = Channel<Command>(10000)

    private val _outputChannel = BroadcastChannel<OutputMessage>(10000)

    val outputChannel get() = _outputChannel.openSubscription()

    var currentFloor = 1
        private set

    var addressedFloor: Int = 1

    var direction = Direction.NONE
        private set

    private var additionalStops: MutableList<Int> = mutableListOf()

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
                                    stopElevatorIfNeeded()
                                }

                                Direction.DOWN -> {
                                    delay(elevatorFloorTravelDurationMs)
                                    currentFloor--
                                    _outputChannel.send(OutputMessage.IsAtFloor(currentFloor, id))
                                    stopElevatorIfNeeded()
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

    private suspend fun stopElevatorIfNeeded() {
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

    fun addAdditionalStop(request: ElevatorRequest) {
        additionalStops = additionalStops
                .plus(listOf(request.requestedFromFloor, request.toFloor))
                .distinct()
                .toMutableList()
    }

    fun moveElevator(request: ElevatorRequest) {
        elevatorScope.launch {
            if (currentFloor == request.requestedFromFloor) {
                commandChannel.send(Command.MoveToFloor(request.toFloor))
            } else {
                commandChannel.send(Command.MoveToFloor(request.requestedFromFloor))
                commandChannel.send(Command.MoveToFloor(request.toFloor))
            }
        }
    }
}
