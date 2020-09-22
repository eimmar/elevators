package com.tingco.codechallenge.elevator

import com.tingco.codechallenge.elevator.config.ElevatorApplication
import com.tingco.codechallenge.elevator.controller.BaseElevatorController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@SpringBootTest(classes = [ElevatorApplication::class])
class IntegrationTest {
    @Autowired
    private lateinit var elevatorController: BaseElevatorController

    @Value("\${com.tingco.elevator.numberofelevators}")
    private val elevatorCount = 0

    @Value("\${com.tingco.elevator.numberoffloors}")
    val floorCount = 0

    @Test
    fun simulateLoadedElevatorShaft() {
        val elevatorRequests = (1..elevatorCount).map { ElevatorRequest(1, floorCount) }
                .plus((1..elevatorCount).map { ElevatorRequest(2, 5) })
                .plus((1..elevatorCount).map { ElevatorRequest(5, 1) })

        elevatorRequests.forEach { elevatorController.requestElevator(it) }
    }
}
