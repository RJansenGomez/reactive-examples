package org.rjansen.event.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.time.Duration


@Repository
class StockRepositorySeq(val stockRestClient: RestTemplate) {
    fun getStock(productId: Int): StockResponse {
        val headers = HttpHeaders()
        headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON.type
        return try {
            stockRestClient.getForEntity("/v1/stock/$productId", StockResponse::class.java, headers).body!!
        } catch (e: HttpStatusCodeException) {
            when (e.statusCode) {
                HttpStatus.BAD_REQUEST -> {
                    throw RuntimeException("Bad request for stock.")
                }
                HttpStatus.NOT_FOUND -> {
                    throw RuntimeException("Stock for product id $productId, not found.")
                }
                else -> {
                    throw RuntimeException(e.statusCode.toString())
                }
            }
        }
    }
}


@Configuration
class StockRestConfiguration {
    @Bean
    fun stockRestClient(
        @Value("\${external.ms.stock") stockUrl: String
    ): RestTemplate = RestTemplateBuilder()
        .rootUri(stockUrl)
        .additionalMessageConverters(MappingJackson2HttpMessageConverter(jacksonObjectMapper()))
        .setConnectTimeout(Duration.ofMillis(5000))
        .setReadTimeout(Duration.ofMillis(5000))
        .build()
}