package com.tingco.codechallenge.elevator.resources

import com.tingco.codechallenge.elevator.config.ElevatorApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@ExperimentalCoroutinesApi
@SpringBootTest(classes = [ElevatorApplication::class])
class ElevatorControllerEndPointsTest {
    //TODO: Use a separate application for tests to avoid usage of main app config values
    @Value("\${com.tingco.elevator.numberoffloors}")
    val floorCount = 0

    @Autowired
    private lateinit var endPoints: ElevatorControllerEndPoints

    @Test
    fun ping() {
        assertEquals("pong", endPoints.ping())
    }

    @Test
    fun requestElevator() {
        assertEquals(1, endPoints.requestElevator(1, floorCount))
    }
}
