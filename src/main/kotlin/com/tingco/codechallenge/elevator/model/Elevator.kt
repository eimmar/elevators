package com.tingco.codechallenge.elevator.model

import com.tingco.codechallenge.elevator.api.IElevator
import com.tingco.codechallenge.elevator.api.IElevator.Direction

class Elevator(override val id: Int, private val floorCount: Int) : IElevator {
    override var addressedFloor: Int? = null
        set(addressedFloor) {
            require(addressedFloor!! <= floorCount) { "Addressed floor exceeds floor limit." }
            field = addressedFloor
        }

    override var currentFloor = 1
        private set

    override var isBusy = false
        private set

    override var direction = Direction.NONE
        private set

    private fun moveElevator() {
        when (direction) {
            Direction.UP ->
//                sleep(1)
                currentFloor++
            Direction.DOWN ->
//                sleep(1)
                currentFloor--
            else -> {}
        }
    }

    override fun moveElevator(toFloor: Int) {
        if (!isBusy) {
            isBusy = true
            direction = if (currentFloor > toFloor) Direction.DOWN else Direction.UP

            while (toFloor != currentFloor) {
                moveElevator()
            }

            isBusy = false
            direction = Direction.NONE
        }
    }
}
