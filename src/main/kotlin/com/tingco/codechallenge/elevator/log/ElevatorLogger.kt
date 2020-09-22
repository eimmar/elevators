package com.tingco.codechallenge.elevator.log

import com.tingco.codechallenge.elevator.model.Elevator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel

@ExperimentalCoroutinesApi
class ElevatorLogger(private val coroutineScope: CoroutineScope) {
    fun subscribeTo(channel: ReceiveChannel<Elevator.OutputMessage>) {
        coroutineScope.launch {
            while (isActive) {
                when (val outputMessage = channel.receive()) {
                    is Elevator.OutputMessage.StoppedAtFloor -> println("Elevator ${outputMessage.elevatorId} stopped at ${outputMessage.floorNumber} floor.")
                    is Elevator.OutputMessage.IsAtFloor -> println("Elevator ${outputMessage.elevatorId} is at ${outputMessage.floorNumber} floor.")
                }
            }
        }
    }
}
