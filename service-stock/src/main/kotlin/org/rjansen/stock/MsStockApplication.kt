package org.rjansen.stock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MsStockApplication

fun main(args: Array<String>) {
    runApplication<MsStockApplication>(*args)
}
