package org.rjansen.event.repository

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity(name = "PURCHASE_LINES")
class PurchaseLineModel {
    @EmbeddedId
    lateinit var purchaseLineId: PurchaseLineId
    var quantity: Int = 0

    @Embeddable
    class PurchaseLineId : Serializable {
        lateinit var purchaseId: String
        lateinit var lineId: String
    }
}