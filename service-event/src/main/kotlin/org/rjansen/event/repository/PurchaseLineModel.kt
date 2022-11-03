package org.rjansen.event.repository

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity("PURCHASE_LINES")
data class PurchaseLineModel(
    @EmbeddedId
    var purchaseLineId: PurchaseLineId,
    var quantity: Int
) {
    @Embeddable
    data class PurchaseLineId(var purchaseId: String, var lineId: String) : Serializable
}