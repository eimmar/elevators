package com.tingco.codechallenge.elevator.resources

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 */
@RestController
@RequestMapping("/rest/v1")
class ElevatorControllerEndPoints {
    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = ["/ping"], method = [RequestMethod.GET])
    fun ping(): String {
        return "pong"
    }
}