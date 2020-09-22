package com.tingco.codechallenge.elevator.log

import com.tingco.codechallenge.elevator.model.BaseElevator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach

@ExperimentalCoroutinesApi
class ElevatorConsoleLogger(private val coroutineScope: CoroutineScope, private val job: Job) {
    fun subscribeTo(channel: ReceiveChannel<BaseElevator.OutputMessage>) {
        coroutineScope.launch(job) {
            channel.consumeEach {
                when (it) {
                    is BaseElevator.OutputMessage.StoppedAtFloor -> println("Elevator ${it.elevatorId} stopped at ${it.floorNumber} floor.")
                    is BaseElevator.OutputMessage.IsAtFloor -> println("Elevator ${it.elevatorId} is at ${it.floorNumber} floor.")
                }
            }
        }
    }
}
