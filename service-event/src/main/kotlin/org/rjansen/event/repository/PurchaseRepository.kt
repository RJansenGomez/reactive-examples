package org.rjansen.event.repository

import org.rjansen.event.service.Purchase
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Id

@Repository
class PurchaseRepository {
    fun create(purchase: Purchase) {

    }
}

interface JpaPurchaseRepository : ReactiveCrudRepository<PurchaseModel, String>
interface JpaPurchaseLinesRepository : ReactiveCrudRepository<PurchaseLineModel, PurchaseLineModel.PurchaseLineId>





