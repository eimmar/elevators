package com.tingco.codechallenge.elevator.controller

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.api.Elevator.Direction
import com.tingco.codechallenge.elevator.api.ElevatorController
import com.tingco.codechallenge.elevator.exception.ElevatorNotFoundException
import com.tingco.codechallenge.elevator.exception.InvalidFloorException
import com.tingco.codechallenge.elevator.log.ElevatorConsoleLogger
import com.tingco.codechallenge.elevator.model.BaseElevator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job

@OptIn(ExperimentalCoroutinesApi::class)
class BaseElevatorController(
        elevatorCount: Int,
        private val floorCount: Int,
        private val elevatorFloorTravelDurationMs: Long,
        private val elevatorScope: CoroutineScope,
        private val elevatorLogger: ElevatorConsoleLogger,
        private val elevatorJob: Job
): ElevatorController {
    override val elevators: List<BaseElevator> = (1..elevatorCount).map {
        BaseElevator(it, elevatorFloorTravelDurationMs, elevatorScope, elevatorJob)
    }

    init {
        elevators.forEach { elevatorLogger.subscribeTo(it.outputChannel) }
    }

    override fun requestElevator(toFloor: Int): Elevator {
        if (toFloor > floorCount) {
            throw InvalidFloorException("Requested floor cannot be greater than $floorCount")
        }

        val elevator = elevators.find { it.getDirection() == Direction.NONE }
        if (elevator != null) {
            elevator.moveElevator(toFloor)

            return elevator
        }
        throw ElevatorNotFoundException()
    }

    fun requestElevator(request: ElevatorRequest): Elevator? {
        if (request.toFloor > floorCount) {
            throw InvalidFloorException("Requested floor cannot be greater than $floorCount")
        }

        val elevator = elevators.find { it.getDirection() == Direction.NONE }
        if (elevator != null) {
            elevator.moveElevator(request)

            return elevator
        }

        if (request.requestedFromFloor > request.toFloor) {
            val descendingElevator = elevators
                    .filter {
                        it.getDirection() == Direction.DOWN
                                && it.getCurrentFloor() > request.requestedFromFloor
                                && it.getAddressedFloor() <= request.toFloor
                    }
                    .minByOrNull { it.getCurrentFloor() }

            descendingElevator?.addAdditionalStop(request)
            return descendingElevator
        } else if (request.requestedFromFloor < request.toFloor) {
            val ascendingElevator = elevators
                    .filter {
                        it.getDirection() == Direction.UP
                                && it.getCurrentFloor() < request.requestedFromFloor
                                && it.getAddressedFloor() >= request.toFloor
                    }
                    .maxByOrNull { it.getCurrentFloor() }

            ascendingElevator?.addAdditionalStop(request)
            return ascendingElevator
        }

        return null
    }
}
