package org.rjansen.stock.controller

import org.rjansen.stock.service.StockService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/stock")
class StockController(val stockService: StockService) {
    @GetMapping("/{productId}")
    fun getStockBy(@PathVariable productId: Mono<Int>): Mono<ResponseEntity<StockResponse>> {
        Thread.sleep(1000)
        stockService.getStockDataFor(productId)
    }
    @GetMapping("/{productId}/s")
    fun getStockBySeq(@PathVariable productId: Int): StockResponse {
        Thread.sleep(1000)
    }
}

data class StockResponse(val productId: Int, val amountAvailable: Int)