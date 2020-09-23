package com.tingco.codechallenge.elevator

import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.config.ElevatorApplication
import com.tingco.codechallenge.elevator.controller.BaseElevatorController
import com.tingco.codechallenge.elevator.log.ElevatorConsoleLogger
import com.tingco.codechallenge.elevator.model.BaseElevator
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@ExperimentalCoroutinesApi
@SpringBootTest(classes = [ElevatorApplication::class])
class IntegrationTest {
    // Since the purpose of this test is to simulate elevatorController
    // functionality, parameters are taken from the configuration file
    @Value("\${com.tingco.elevator.numberofelevators}")
    private val elevatorCount = 0

    @Value("\${com.tingco.elevator.numberoffloors}")
    val floorCount = 0

    @Value("\${com.tingco.elevator.elevatorfloortraveldurationms}")
    private val elevatorFloorTravelDurationMs = 0L

    @Test
    fun simulateLoadedElevatorShaft() = runBlockingTest {
        val job = Job()
        val elevatorController = BaseElevatorController(
                (1..elevatorCount).map { BaseElevator(it, elevatorFloorTravelDurationMs, this, job) },
                floorCount,
                ElevatorConsoleLogger(this, job),
        )

        val elevatorRequests: List<Pair<Long, ElevatorRequest>> = (1..elevatorCount).map {
            100L to ElevatorRequest(1, 4)
        }.plus(listOf(
                500L to ElevatorRequest(2, 4),
                500L to ElevatorRequest(3, 8),
                10000L to ElevatorRequest(5, 1),
                4000L to ElevatorRequest(4, 1),
                300L to ElevatorRequest(4, 1),
                300L to ElevatorRequest(4, 1),
                500L to ElevatorRequest(3, 2),
        ))

        elevatorRequests.forEach {
            advanceTimeBy(it.first)
            elevatorController.requestElevator(it.second)
        }

        advanceTimeBy(elevatorFloorTravelDurationMs * elevatorRequests.size * 10)
        assertTrue { elevatorController.elevators.map { it.getDirection() }.all { it == Elevator.Direction.NONE } }

        job.cancelAndJoin()
        cleanupTestCoroutines()
    }
}
