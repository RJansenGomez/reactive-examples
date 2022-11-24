package org.rjansen.event

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class MsServiceApplication

fun main(args: Array<String>) {
    runApplication<MsServiceApplication>(*args)
}