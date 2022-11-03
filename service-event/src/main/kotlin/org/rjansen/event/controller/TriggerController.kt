package org.rjansen.event.controller

import org.rjansen.event.service.Purchase
import org.rjansen.event.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/trigger")
class TriggerController(val purchaseService: PurchaseService) {

    @PostMapping
    fun triggerFlow(@RequestBody purchaseRequest: Mono<PurchaseDto>): Mono<ResponseEntity<String>> {
        return purchaseRequest
            .handle { dto, sink ->
                try {
                    purchaseService.storePurchase(Mono.just(dto.toEntity()))
                    sink.next(ResponseEntity.status(HttpStatus.CREATED).body("Created"))
                } catch (e: Exception) {
                    sink.next(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.message))
                }
            }
    }
    @PostMapping("/b")
    fun triggerBFlow(@RequestBody purchaseRequest: Mono<PurchaseDto>): Mono<ResponseEntity<String>> {
        return purchaseRequest
            .handle { dto, sink ->
                try {
                    purchaseService.storePurchase(Mono.just(dto.toEntity()))
                    sink.next(ResponseEntity.status(HttpStatus.CREATED).body("Created"))
                } catch (e: Exception) {
                    sink.error(MonoError(e))
                }
            }
    }

    @PostMapping("/sequential")
    fun triggerSequentialFlow(@RequestBody purchaseRequest: PurchaseDto): ResponseEntity<String> {
        purchaseService.storePurchase(purchaseRequest.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Created")
    }
}


data class PurchaseDto(val customerId: String, val products: Map<Int,Int>, val discount: Double, val totalAmount: Double) {
    fun toEntity(): Purchase = Purchase(customerId, products, discount, totalAmount)
}