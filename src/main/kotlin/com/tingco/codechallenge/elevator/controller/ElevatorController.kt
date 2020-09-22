package com.tingco.codechallenge.elevator.controller

import ElevatorRequest
import com.tingco.codechallenge.elevator.log.ElevatorLogger
import com.tingco.codechallenge.elevator.model.Elevator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ElevatorController(
        elevatorCount: Int,
        private val floorCount: Int,
        private val elevatorFloorTravelDurationMs: Long,
        private val elevatorScope: CoroutineScope,
        private val elevatorLogger: ElevatorLogger
) {
    private val elevators: List<Elevator> = (1..elevatorCount).map {
        Elevator(it, elevatorFloorTravelDurationMs, elevatorScope)
    }

    init {
        elevators.forEach { elevatorLogger.subscribeTo(it.outputChannel) }
    }

    fun requestElevator(request: ElevatorRequest): Elevator? {
        if (request.toFloor >= floorCount) {
            return null
        }

        val elevator = elevators.find { it.direction == Direction.NONE }
        if (elevator != null) {
            elevator.moveElevator(request)

            return elevator
        }

        if (request.requestedFromFloor > request.toFloor) {
            val descendingElevator = elevators
                    .filter {
                        it.direction == Direction.DOWN
                                && it.currentFloor > request.requestedFromFloor
                                && it.addressedFloor <= request.toFloor
                    }
                    .minByOrNull { it.currentFloor }

            descendingElevator?.addAdditionalStop(request)
            return descendingElevator
        } else if (request.requestedFromFloor < request.toFloor) {
            val ascendingElevator = elevators
                    .filter {
                        it.direction == Direction.UP
                                && it.currentFloor < request.requestedFromFloor
                                && it.addressedFloor >= request.toFloor
                    }
                    .maxByOrNull { it.currentFloor }

            ascendingElevator?.addAdditionalStop(request)
            return ascendingElevator
        }

        return null
    }
}
