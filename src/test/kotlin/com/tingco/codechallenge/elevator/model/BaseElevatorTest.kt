package com.tingco.codechallenge.elevator.model

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.api.Elevator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class BaseElevatorTest {
    private val coroutineScope = TestCoroutineScope(TestCoroutineDispatcher())
    private val job = Job()
    private val elevator = BaseElevator(1, 2000, coroutineScope, job)

    @Test
    fun testMoveElevatorToFloor() = runBlockingTest(coroutineScope.coroutineContext) {
        elevator.moveElevator(3)

        advanceTimeBy(500)
        assertTrue(elevator.isBusy())
        assertEquals(Elevator.Direction.UP, elevator.getDirection())
        assertEquals(3, elevator.getAddressedFloor())

        advanceTimeBy(1500)
        assertEquals(2, elevator.getCurrentFloor())

        advanceTimeBy(4000)
        assertFalse(elevator.isBusy())
        assertEquals(Elevator.Direction.NONE, elevator.getDirection())
        assertEquals(3, elevator.getCurrentFloor())
    }

    @Test
    fun testMoveElevatorWhenRequestedFromAnotherFloor() = runBlockingTest(coroutineScope.coroutineContext) {
        elevator.moveElevator(ElevatorRequest(3, 5))

        advanceTimeBy(6000)
        assertEquals(3, elevator.getCurrentFloor())

        advanceTimeBy(4000)
        assertEquals(5, elevator.getCurrentFloor())
    }


    @Test
    fun testMoveElevatorWithAdditionalStops() = runBlockingTest(coroutineScope.coroutineContext) {
        elevator.moveElevator(ElevatorRequest(1, 8))
        elevator.addAdditionalStop(ElevatorRequest(3, 5))
        elevator.addAdditionalStop(ElevatorRequest(3, 6))

        advanceTimeBy(6000)
        assertEquals(3, elevator.getCurrentFloor())

        advanceTimeBy(6000)
        assertEquals(5, elevator.getCurrentFloor())

        advanceTimeBy(4000)
        assertEquals(6, elevator.getCurrentFloor())

        advanceTimeBy(4000)
        assertEquals(8, elevator.getCurrentFloor())
    }
}
