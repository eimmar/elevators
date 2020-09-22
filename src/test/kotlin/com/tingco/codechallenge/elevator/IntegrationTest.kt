package com.tingco.codechallenge.elevator

import com.tingco.codechallenge.elevator.api.Elevator
import com.tingco.codechallenge.elevator.config.ElevatorApplication
import com.tingco.codechallenge.elevator.controller.BaseElevatorController
import com.tingco.codechallenge.elevator.log.ElevatorConsoleLogger
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
@SpringBootTest(classes = [ElevatorApplication::class])
class IntegrationTest {
    @Value("\${com.tingco.elevator.numberofelevators}")
    private val elevatorCount = 0

    @Value("\${com.tingco.elevator.numberoffloors}")
    val floorCount = 0

    @Value("\${com.tingco.elevator.elevatorfloortraveldurationms}")
    private val elevatorFloorTravelDurationMs = 0L

    @ExperimentalCoroutinesApi
    @Test
    fun simulateLoadedElevatorShaft() = runBlockingTest {
        val job = Job()
        val elevatorController = BaseElevatorController(
                elevatorCount,
                floorCount,
                elevatorFloorTravelDurationMs,
                this,
                ElevatorConsoleLogger(this, job),
                job
        )

        val elevatorRequests = (1..elevatorCount).map { ElevatorRequest(1, floorCount) }
                .plus((1..elevatorCount).map { ElevatorRequest(3, 5) })
                .plus((1..elevatorCount).map { ElevatorRequest(5, 1) })

        elevatorRequests.forEach {
            elevatorController.requestElevator(it)
            advanceTimeBy(1000)
        }

        assertTrue { elevatorController.elevators.map { it.getDirection() }.all { it == Elevator.Direction.UP } }
        advanceTimeBy(elevatorFloorTravelDurationMs * elevatorRequests.size * 10)

        job.cancelAndJoin()
        cleanupTestCoroutines()
    }
}
