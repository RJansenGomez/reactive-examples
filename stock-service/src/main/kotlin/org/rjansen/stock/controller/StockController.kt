package org.rjansen.stock.controller

import org.rjansen.stock.service.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/stock")
class StockController(val stockService: StockService) {
    @GetMapping("/{productId}")
    fun getStockBy(@PathVariable productId: Mono<Int>): Mono<ResponseEntity<*>> {
        return stockService.getStockDataFor(productId).map { toResponse(it) }
    }

    @GetMapping("/{productId}/s")
    fun getStockBySeq(@PathVariable productId: Int): ResponseEntity<*> {
        return toResponse(stockService.getStockDataFor(productId))
    }

    @PostMapping("/{productId}/remove/seq")
    fun removeStockSeq(@PathVariable productId: Int, @RequestBody amount: Int): ResponseEntity<String> {
        return toResponse(stockService.remove(StockDataFound(productId, amount)))
    }

    @PostMapping("/{productId}/remove")
    fun removeStock(@PathVariable productId: Mono<Int>, @RequestBody amount: Mono<Int>): Mono<ResponseEntity<String>> {
        return stockService.remove(
            Mono.zip(productId, amount)
                .map { tuple2 -> StockDataFound(tuple2.t1, tuple2.t2) }
        ).map { toResponse(it) }
    }

    fun toResponse(stockData: StockData): ResponseEntity<*> {
        return when (stockData) {
            is StockDataFound -> ResponseEntity.ok(StockResponse.from(stockData))
            is StockDataNotFound -> notFound(stockData)
        }
    }

    fun toResponse(removeResponse: RemoveResponse): ResponseEntity<String> {
        return when (removeResponse) {
            is Success -> ResponseEntity.ok("Success")
            is NotPossible -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Not enough stock for product ${removeResponse.productId}, ${removeResponse.amountOverFlow} missing stock")

            is StockDataNotFound -> notFound(removeResponse)
        }
    }

    private fun notFound(stockData: StockDataNotFound) =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Stock not found for product ${stockData.productId}")
}

data class StockResponse(val productId: Int, val amountAvailable: Int) {
    companion object {
        fun from(stockData: StockDataFound): StockResponse =
            StockResponse(stockData.productId, stockData.amount)
    }
}