package com.tingco.codechallenge.elevator.controller

import com.google.common.eventbus.EventBus
import com.tingco.codechallenge.elevator.api.IElevator
import com.tingco.codechallenge.elevator.api.IElevatorController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.util.concurrent.Executor

class ElevatorController : IElevatorController {
    @Autowired
    lateinit var eventBus: EventBus

    @Autowired
    lateinit var executor: Executor

    @Value("\${com.tingco.elevator.numberoffloors}")
    val numberOfFloors: Int = 0

    override fun requestElevator(toFloor: Int): IElevator? {
        return null
    }

    override val elevators: List<IElevator>
        get() = listOf()

    override fun releaseElevator(elevator: IElevator) {}
}
