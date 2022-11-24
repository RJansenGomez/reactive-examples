package org.rjansen.event.service

import org.rjansen.event.repository.PurchaseRepository
import org.rjansen.event.repository.StockRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class PurchaseService(
    val stockRepository: StockRepository,
    val purchaseRepository: PurchaseRepository
) {
    fun storePurchase(monoPurchase: Mono<Purchase>) {
        monoPurchase.subscribe {
            Flux.fromIterable(it.products.entries)
                .publishOn(Schedulers.parallel())
                .subscribe { ::checkStock }
            purchaseRepository.create(it)
        }
        throw RuntimeException("I want to fail")
    }

    private fun checkStock(entry: Pair<Int, Int>) {
        println("Checking stock for ${entry.first}")
        stockRepository.getStock(entry.first).subscribe {
            if (it.amountAvailable < entry.second) {
                println("Failing on ${entry.first}")
                throw NotEnoughStock(entry.first, it.amountAvailable, entry.second)
            }
        }
    }

    fun storePurchase(purchase: Purchase) {
        throw RuntimeException("I want to fail")
    }
}

data class Purchase(val customerId: String, val products: Map<Int, Int>, val discount: Double, val totalAmount: Double)