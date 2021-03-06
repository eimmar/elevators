package com.tingco.codechallenge.elevator.resources

import com.tingco.codechallenge.elevator.ElevatorRequest
import com.tingco.codechallenge.elevator.controller.BaseElevatorController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
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
    @Autowired
    lateinit var elevatorController: BaseElevatorController

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = ["/ping"], method = [RequestMethod.GET])
    fun ping(): String {
        return "pong"
    }

    @RequestMapping(value = ["/request-elevator/{requestedFromFloor}/{toFloor}"], method = [RequestMethod.GET])
    fun requestElevator(
            @PathVariable(value = "requestedFromFloor") requestedFromFloor: Int,
            @PathVariable(value = "toFloor") toFloor: Int
    ): Int? {
        return elevatorController.requestElevator(ElevatorRequest(requestedFromFloor, toFloor))?.id
    }
}
