package com.tingco.codechallenge.elevator.resources

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootApplication
class ElevatorControllerEndPointsTest {
    @Autowired
    private lateinit var endPoints: ElevatorControllerEndPoints

    @Test
    fun ping() {
        Assert.assertEquals("pong", endPoints.ping())
    }
}
