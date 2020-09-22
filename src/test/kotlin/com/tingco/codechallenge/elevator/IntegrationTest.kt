package com.tingco.codechallenge.elevator

import ElevatorRequest
import com.tingco.codechallenge.elevator.config.ElevatorApplication
import com.tingco.codechallenge.elevator.controller.ElevatorController
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [ElevatorApplication::class])
class IntegrationTest {
//    @Autowired
//    private lateinit var elevatorController: ElevatorController
//
//    @Value("\${com.tingco.elevator.numberofelevators}")
//    private val elevatorCount = 0
//
//    @Value("\${com.tingco.elevator.numberoffloors}")
//    private val floorCount = 0

    @Test
    fun simulateLoadedElevatorShaft() {
//        val elevatorRequests = (1..elevatorCount).map { ElevatorRequest(1, floorCount) }
//                .plus((1..elevatorCount).map { ElevatorRequest(2, 5) })
//                .plus((1..elevatorCount).map { ElevatorRequest(5, 1) })
//
//        elevatorRequests.forEach{ elevatorController.requestElevator(it) }
    }
}
