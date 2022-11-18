package org.rjansen.stock.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class StockService {
    fun getStockDataFor(productId: Int): StockData {

    }
    fun getStockDataFor(productId: Mono<Int>): Mono<StockData> {

    }
}

data class StockData(val productId: Int, val amountAvailable: Int)