package org.rjansen.stock.repository

import javax.persistence.Entity
import javax.persistence.Id

interface StockRepository {
}

@Entity(name = "stock")
class StockModel {
    @Id
    var productId: Int = 0
    var totalAmount: Int = 0
}