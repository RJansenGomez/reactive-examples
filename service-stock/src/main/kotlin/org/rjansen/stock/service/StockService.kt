package org.rjansen.stock.service

import org.rjansen.stock.repository.SeqStockRepository
import org.rjansen.stock.repository.StockModel
import org.rjansen.stock.repository.StockRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.math.abs

@Service
class StockService(
    val seqStockRepository: SeqStockRepository,
    val stockRepository: StockRepository
) {
    fun getStockDataFor(productId: Int): StockData {
        delay()
        return seqStockRepository.findById(productId)
            .map(StockModel::toEntity)
            .orElse(StockDataNotFound(productId))
    }

    fun getStockDataFor(productId: Mono<Int>): Mono<StockData> {
        delay()
        return stockRepository.findById(productId)
            .map(StockModel::toEntity)
            .switchIfEmpty(productId.map { StockDataNotFound(it) })
    }

    fun remove(stockData: Mono<StockDataFound>): Mono<RemoveResponse> {
        val currentStock = getStockDataFor(stockData.map { it.productId })
        return Mono.zip(stockData, currentStock)
            .map { tuple2 ->
                val stock = tuple2.t1
                when (val current = tuple2.t2) {
                    is StockDataFound -> removeStock(stock, current)
                    is StockDataNotFound -> current
                }
            }
    }

    fun remove(stockData: StockDataFound): RemoveResponse {
        return when (val currentStock = getStockDataFor(stockData.productId)) {
            is StockDataFound -> removeStock(stockData, currentStock)
            is StockDataNotFound -> currentStock
        }
    }

    private fun removeStock(toRemove: StockDataFound, current: StockDataFound): RemoveResponse {
        smallDelay()
        val newAmount = current.amount - toRemove.amount
        return if (newAmount >= 0) {
            stockRepository.save(StockModel.from(StockDataFound(current.productId, newAmount)))
            Success(current.productId, newAmount)
        } else {
            NotPossible(current.productId, abs(newAmount))
        }
    }

    private fun delay() {
        Thread.sleep(500)
    }

    private fun smallDelay() {
        Thread.sleep(100)
    }
}

sealed interface StockData
data class StockDataFound(val productId: Int, val amount: Int) : StockData
data class StockDataNotFound(val productId: Int) : StockData, RemoveResponse

sealed interface RemoveResponse
data class Success(val productId: Int, val amount: Int) : RemoveResponse
data class NotPossible(val productId: Int, val amountOverFlow: Int) : RemoveResponse
