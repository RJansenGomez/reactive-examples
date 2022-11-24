package org.rjansen.stock.repository

import org.rjansen.stock.service.StockData
import org.rjansen.stock.service.StockDataFound
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface StockRepository : ReactiveCrudRepository<StockModel, Int>

@Entity(name = "stock")
class StockModel(
    @Id
    var productId: Int = 0,
    var totalAmount: Int = 0
) {


    fun toEntity(): StockData = StockDataFound(productId, totalAmount)

    companion object {
        fun from(stockData: StockDataFound): StockModel {
            return StockModel(stockData.productId, stockData.amount)
        }
    }
}