package com.tingco.codechallenge.elevator.resources

import com.tingco.codechallenge.elevator.api.IElevatorController
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
    lateinit var elevatorController: IElevatorController

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = ["/ping"], method = [RequestMethod.GET])
    fun ping(): String {
        return "pong"
    }

    /**
     *
     * @param floor Floor to request
     * @return Integer
     */
    @RequestMapping(value = ["/request-elevator/{floor}"], method = [RequestMethod.GET])
    fun requestElevator(@PathVariable(value = "floor") floor: Int): Int {
        elevatorController.requestElevator(floor)
        return floor
    }
}
