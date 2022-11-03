package org.rjansen.event.repository

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "PURCHASE")
class PurchaseModel {
    @Id
    lateinit var id: String
    lateinit var customerId: String
    var discount: Double = 0.0
    var amount: Double = 0.0
}
