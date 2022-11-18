package org.rjansen.event.service

import org.rjansen.event.repository.PurchaseRepositorySeq
import org.rjansen.event.repository.StockRepositorySeq
import org.springframework.stereotype.Service

@Service
class PurchaseServiceSeq(
    val stockRepository: StockRepositorySeq,
    val purchaseRepository: PurchaseRepositorySeq
) {
    fun storePurchase(purchase: Purchase, parallel: Boolean) {
        if (parallel) {
            purchase.products.entries.parallelStream().forEach(this::checkStock)
        } else {
            purchase.products.forEach(this::checkStock)
        }
        purchaseRepository.create(purchase)
    }

    private fun checkStock(entry: Map.Entry<Int, Int>) {
        println("Checking stock for ${entry.key}")
        val response = stockRepository.getStock(entry.key)
        if (response.amountAvailable < entry.value) {
            println("Failing on ${entry.key}")
            throw NotEnoughStock(entry.key, response.amountAvailable, entry.value)
        }
    }

}



