package com.tingco.codechallenge.elevator.config

import com.tingco.codechallenge.elevator.api.ElevatorController
import com.tingco.codechallenge.elevator.controller.BaseElevatorController
import com.tingco.codechallenge.elevator.log.ElevatorConsoleLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.*

/**
 * Preconfigured Spring Application boot class.
 *
 */
@ExperimentalCoroutinesApi
@Configuration
@ComponentScan(basePackages = ["com.tingco.codechallenge.elevator"])
@EnableAutoConfiguration
@PropertySources(PropertySource("classpath:application.properties"))
@SpringBootApplication
class ElevatorApplication {
    @Value("\${com.tingco.elevator.numberofelevators}")
    private val elevatorCount = 0

    @Value("\${com.tingco.elevator.numberoffloors}")
    private val floorCount = 0

    @Value("\${com.tingco.elevator.elevatorfloortraveldurationms}")
    private val elevatorFloorTravelDurationMs = 0L

    @Bean
    fun elevatorScope(): CoroutineScope = CoroutineScope(Dispatchers.Default)

    @Bean
    fun elevatorLogger(elevatorScope: CoroutineScope): ElevatorConsoleLogger = ElevatorConsoleLogger(
            CoroutineScope(Dispatchers.Default),
            Job()
    )

    @Bean
    fun elevatorController(elevatorLogger: ElevatorConsoleLogger): BaseElevatorController
            = BaseElevatorController(
            elevatorCount,
            floorCount,
            elevatorFloorTravelDurationMs,
            CoroutineScope(Dispatchers.Default),
            elevatorLogger,
            Job()
    )
}

@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
    runApplication<ElevatorApplication>(*args)
}
