package com.tingco.codechallenge.elevator.resources

import com.tingco.codechallenge.elevator.config.ElevatorApplication
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [ElevatorApplication::class])
class ElevatorControllerEndPointsTest {
    @Autowired
    private lateinit var endPoints: ElevatorControllerEndPoints

    @Test
    fun ping() {
        assertEquals("pong", endPoints.ping())
    }
}
