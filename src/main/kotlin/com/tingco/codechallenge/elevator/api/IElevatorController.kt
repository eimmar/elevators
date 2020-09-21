package com.tingco.codechallenge.elevator.api

/**
 * Interface for the Elevator Controller.
 *
 * @author Sven Wesley
 */
interface IElevatorController {
    /**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all [IElevator] objects.
     */
    val elevators: List<IElevator>

    /**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     * addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
    fun requestElevator(toFloor: Int): IElevator?

    /**
     * Telling the controller that the given elevator is free for new
     * operations.
     *
     * @param elevator
     * the elevator that shall be released.
     */
    fun releaseElevator(elevator: IElevator)
}
