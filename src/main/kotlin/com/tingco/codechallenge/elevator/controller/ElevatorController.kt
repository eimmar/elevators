package com.tingco.codechallenge.elevator.controller

import com.google.common.eventbus.EventBus
import com.tingco.codechallenge.elevator.api.IElevator
import com.tingco.codechallenge.elevator.api.IElevatorController
import java.util.concurrent.Executor

class ElevatorController(
        private val eventBus: EventBus,
        private val executor: Executor,
        private val numberOfFloors: Int
) : IElevatorController {
    override fun requestElevator(toFloor: Int): IElevator? {
        return null
    }

    override val elevators: List<IElevator>
        get() = listOf()

    override fun releaseElevator(elevator: IElevator) {}
}
