package org.rjansen.stock.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface SeqStockRepository : JpaRepository<StockModel, Int> {
}