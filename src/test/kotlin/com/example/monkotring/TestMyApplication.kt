package com.example.monkotring

import org.springframework.boot.SpringApplication
import org.springframework.boot.devtools.restart.RestartScope
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MongoDBContainer

class TestMyApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.from(MonkotringApplication::main).with(MyContainersConfiguration::class.java).run(*args)
        }
    }
}

@TestConfiguration(proxyBeanMethods = false)
class MyContainersConfiguration {

    @Bean
    @ServiceConnection
    @RestartScope
    fun mongodb() = MongoDBContainer("mongo:4.4.2")
}