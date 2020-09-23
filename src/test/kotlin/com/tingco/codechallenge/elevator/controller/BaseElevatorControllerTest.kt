package com.tingco.codechallenge.elevator.controller

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.exception.ElevatorNotFoundException
import com.tingco.codechallenge.elevator.exception.InvalidFloorException
import com.tingco.codechallenge.elevator.model.BaseElevator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class BaseElevatorControllerTest {
    private val elevators = (1..4).map { mockk<BaseElevator>(relaxed = true) }

    private val controller = BaseElevatorController(elevators, 9, mockk(relaxed = true))

    @Test
    fun testRequestElevator() {
        coEvery { elevators[0].isBusy() } returns true
        coEvery { elevators[1].isBusy() } returns true
        coEvery { elevators[2].isBusy() } returns false
        coEvery { elevators[3].isBusy() } returns true

        assertEquals(elevators[2], controller.requestElevator(1))
    }

    @Test
    fun testRequestElevatorWhenAllAreBusy() {
        elevators.map { coEvery { it.isBusy() } returns true }

        assertFailsWith<ElevatorNotFoundException> { controller.requestElevator(1) }
    }

    @Test
    fun testRequestElevatorToInvalidFloor() {
        assertFailsWith<InvalidFloorException> { controller.requestElevator(10) }
        assertFailsWith<InvalidFloorException> { controller.requestElevator(0) }
    }

    @Test
    fun testRequestElevatorToInvalidFloorForRequest() {
        assertFailsWith<InvalidFloorException> { controller.requestElevator(ElevatorRequest(1, 10)) }
        assertFailsWith<InvalidFloorException> { controller.requestElevator(ElevatorRequest(0, 5)) }
    }

    @Test
    fun testRequestElevatorWithRequest() {
        coEvery { elevators[0].isBusy() } returns true
        coEvery { elevators[1].isBusy() } returns true
        coEvery { elevators[2].isBusy() } returns false
        coEvery { elevators[3].isBusy() } returns true

        assertEquals(elevators[2], controller.requestElevator(ElevatorRequest(1, 5)))
    }

    @Test
    fun testRequestAscendingElevator() {
        elevators.map { coEvery { it.isBusy() } returns true }
        elevators.map { coEvery { it.getDirection() } returns Elevator.Direction.UP }
        elevators.map { coEvery { it.getCurrentFloor() } returns 3 }

        coEvery { elevators[2].getCurrentFloor() } returns 2

        assertEquals(elevators[2], controller.requestElevator(ElevatorRequest(3, 5)))
        assertNull(controller.requestElevator(ElevatorRequest(2, 5)))
        coVerify(exactly = 1) { elevators[2].addAdditionalStop(ElevatorRequest(3, 5)) }
    }

    @Test
    fun testRequestDescendingElevator() {
        elevators.map { coEvery { it.isBusy() } returns true }
        elevators.map { coEvery { it.getDirection() } returns Elevator.Direction.DOWN }
        elevators.map { coEvery { it.getCurrentFloor() } returns 5 }

        coEvery { elevators[2].getCurrentFloor() } returns 6

        assertEquals(elevators[2], controller.requestElevator(ElevatorRequest(5, 3)))
        assertNull(controller.requestElevator(ElevatorRequest(6, 2)))
        coVerify(exactly = 1) { elevators[2].addAdditionalStop(ElevatorRequest(5, 3)) }
    }
}
