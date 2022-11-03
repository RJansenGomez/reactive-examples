package org.rjansen.event.repository

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "PURCHASE")
data class PurchaseModel(
    @Id
    var id: String,
    var customerId: String,
    var discount: Double,
    var amount: Double
)