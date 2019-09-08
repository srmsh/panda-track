package org.track.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class PandaServer

fun main(args: Array<String>) {
    SpringApplication.run(PandaServer::class.java, *args)
}