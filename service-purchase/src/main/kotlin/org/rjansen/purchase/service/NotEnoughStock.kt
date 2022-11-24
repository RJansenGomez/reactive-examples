package org.rjansen.event.service

class NotEnoughStock(productId: Int, quantityAvailable: Int, quantityRequested: Int) :
    RuntimeException("Not enough stock for $productId, available stock: $quantityAvailable, requested: $quantityRequested")