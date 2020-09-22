package com.tingco.codechallenge.elevator.api

/**
 * Interface for the Elevator Controller.
 *
 * @author Sven Wesley
 */
interface ElevatorController {
    /**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     * addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
    fun requestElevator(toFloor: Int): Elevator

    /**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all [Elevator] objects.
     */
    val elevators: List<Elevator>
}