package com.tingco.codechallenge.elevator.controller

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.api.Elevator.Direction
import com.tingco.codechallenge.elevator.api.ElevatorController
import com.tingco.codechallenge.elevator.exception.ElevatorNotFoundException
import com.tingco.codechallenge.elevator.exception.InvalidFloorException
import com.tingco.codechallenge.elevator.log.ElevatorConsoleLogger
import com.tingco.codechallenge.elevator.model.BaseElevator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BaseElevatorController(
        override val elevators: List<BaseElevator>,
        private val floorCount: Int,
        private val elevatorLogger: ElevatorConsoleLogger,
): ElevatorController {
    init {
        elevators.forEach { elevatorLogger.subscribeTo(it.outputChannel) }
    }

    private fun isRequestValid(request: ElevatorRequest) =
            !(request.toFloor > floorCount || request.toFloor < 1)
                    && !(request.requestedFromFloor > floorCount || request.requestedFromFloor < 1)

    override fun requestElevator(toFloor: Int): Elevator {
        if (toFloor > floorCount || toFloor < 1) {
            throw InvalidFloorException("Requested floor cannot be greater than $floorCount")
        }

        val elevator = elevators.find { !it.isBusy() }
        if (elevator != null) {
            elevator.moveElevator(toFloor)

            return elevator
        }
        throw ElevatorNotFoundException()
    }

    fun requestElevator(request: ElevatorRequest): Elevator? {
        if (!isRequestValid(request)) {
            throw InvalidFloorException("Requested floor cannot be greater than $floorCount")
        }

        //TODO: Instead of always looking for free elevators first check for available elevator that are moving the same
        // direction as requested
        val elevator = elevators.find { !it.isBusy() }
        if (elevator != null) {
            // Helps testing for the sake of this challenge but otherwise it is not needed and would be removed
            println("Found elevator with ID ${elevator.id} for request from ${request.requestedFromFloor} to ${request.toFloor}")

            elevator.moveElevator(request)

            return elevator
        }

        if (request.requestedFromFloor > request.toFloor) {
            val descendingElevator = elevators
                    .filter { it.getDirection() == Direction.DOWN && it.getCurrentFloor() > request.requestedFromFloor }
                    .minByOrNull { it.getCurrentFloor() }

            // Helps testing for the sake of this challenge but otherwise it is not needed and would be removed
            println("Found descending elevator with ID ${descendingElevator?.id} for request from ${request.requestedFromFloor} to ${request.toFloor}")

            descendingElevator?.addAdditionalStop(request)
            return descendingElevator
        } else if (request.requestedFromFloor < request.toFloor) {
            val ascendingElevator = elevators
                    .filter { it.getDirection() == Direction.UP && it.getCurrentFloor() < request.requestedFromFloor }
                    .maxByOrNull { it.getCurrentFloor() }

            // Helps testing for the sake of this challenge but otherwise it is not needed and would be removed
            println("Found ascending elevator with ID ${ascendingElevator?.id} for request from ${request.requestedFromFloor} to ${request.toFloor}")

            ascendingElevator?.addAdditionalStop(request)
            return ascendingElevator
        }

        return null
    }
}
