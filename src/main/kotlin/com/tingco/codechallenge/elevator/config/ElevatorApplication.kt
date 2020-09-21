package com.tingco.codechallenge.elevator.config

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Preconfigured Spring Application boot class.
 *
 */
@Configuration
@ComponentScan(basePackages = ["com.tingco.codechallenge.elevator"])
@EnableAutoConfiguration
@PropertySources(PropertySource("classpath:application.properties"))
class ElevatorApplication {
    @Value("\${com.tingco.elevator.numberofelevators}")
    private val numberOfElevators = 0

    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
    @Bean(destroyMethod = "shutdown")
    fun taskExecutor(): Executor {
        return Executors.newScheduledThreadPool(numberOfElevators)
    }

    /**
     * Create an event bus for your convenience.
     *
     * @return EventBus for async task execution
     */
    @Bean
    fun eventBus(): EventBus {
        return AsyncEventBus(Executors.newCachedThreadPool())
    }
}

fun main(args: Array<String>) {
    runApplication<ElevatorApplication>(*args)
}
