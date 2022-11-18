package org.rjansen.event.controller

import kotlinx.coroutines.*
import org.rjansen.event.service.Purchase
import org.rjansen.event.service.PurchaseService
import org.rjansen.event.service.PurchaseServiceSeq
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/trigger")
class TriggerController(
    val purchaseService: PurchaseService,
    val purchaseServiceSeq: PurchaseServiceSeq
) {

    @PostMapping
    fun triggerFlow(@RequestBody purchaseRequest: Mono<PurchaseDto>): Mono<ResponseEntity<String>> {
        return try {
            purchaseService.storePurchase(purchaseRequest.map { it.toEntity() })
            Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("Created"))
        }catch (e:Exception){
            Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.message))
        }
//        return purchaseRequest
//            .handle { dto, sink ->
//                try {
//                    purchaseService.storePurchase(Mono.just(dto.toEntity()))
//                    sink.next(ResponseEntity.status(HttpStatus.CREATED).body("Created"))
//                } catch (e: Exception) {
//                    sink.next(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.message))
//                }
//            }
    }

    @PostMapping("/b")
    suspend fun triggerBFlow(@RequestBody purchaseRequest: PurchaseDto): Deferred<ResponseEntity<String>> {
       return CoroutineScope(Dispatchers.Default).async {
            purchaseServiceSeq.storePurchase(purchaseRequest.toEntity(),true)
            ResponseEntity.status(HttpStatus.CREATED).body("Created")
        }
    }


    @PostMapping("/sequential")
    fun triggerSequentialFlow(@RequestBody purchaseRequest: PurchaseDto): ResponseEntity<String> {
        purchaseService.storePurchase(purchaseRequest.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Created")
    }
}


data class PurchaseDto(
    val customerId: String,
    val products: Map<Int, Int>,
    val discount: Double,
    val totalAmount: Double
) {
    fun toEntity(): Purchase = Purchase(customerId, products, discount, totalAmount)
}