package com.tingco.codechallenge.elevator.api

/**
 * Interface for an elevator object.
 *
 * @author Sven Wesley
 */
interface IElevator {
    /**
     * Enumeration for describing elevator's direction.
     */
    enum class Direction {
        UP, DOWN, NONE
    }

    /**
     * Tells which direction is the elevator going in.
     *
     * @return Direction Enumeration value describing the direction.
     */
    val direction: Direction

    /**
     * If the elevator is moving. This is the target floor.
     *
     * @return primitive integer number of floor
     */
    val addressedFloor: Int?

    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    val id: Int

    /**
     * Check if the elevator is occupied at the moment.
     *
     * @return true if busy.
     */
    val isBusy: Boolean

    /**
     * Reports which floor the elevator is at right now.
     *
     * @return int actual floor at the moment.
     */
    val currentFloor: Int

    /**
     * Command to move the elevator to the given floor.
     *
     * @param toFloor
     */
    fun moveElevator(toFloor: Int)
}
