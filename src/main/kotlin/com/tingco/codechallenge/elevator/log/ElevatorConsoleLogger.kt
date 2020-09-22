package com.tingco.codechallenge.elevator.log

import com.tingco.codechallenge.elevator.model.BaseElevator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel

@ExperimentalCoroutinesApi
class ElevatorConsoleLogger(private val coroutineScope: CoroutineScope) {
    fun subscribeTo(channel: ReceiveChannel<BaseElevator.OutputMessage>) {
        coroutineScope.launch {
            while (isActive) {
                when (val outputMessage = channel.receive()) {
                    is BaseElevator.OutputMessage.StoppedAtFloor -> println("Elevator ${outputMessage.elevatorId} stopped at ${outputMessage.floorNumber} floor.")
                    is BaseElevator.OutputMessage.IsAtFloor -> println("Elevator ${outputMessage.elevatorId} is at ${outputMessage.floorNumber} floor.")
                }
            }
        }
    }
}
