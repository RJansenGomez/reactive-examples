package org.rjansen.event.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit


@Repository
class StockRepository(val stockWebClient: WebClient) {
    fun getStock(productId: Int): Mono<StockResponse> {
        return stockWebClient.get()
            .uri("/v1/stock/$productId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ obj: HttpStatus -> obj.isError }) { response: ClientResponse ->
                when (response.statusCode()) {
                    HttpStatus.BAD_REQUEST -> {
                        Mono.error(RuntimeException("Bad request for stock."))
                    }
                    HttpStatus.NOT_FOUND -> {
                        Mono.error(RuntimeException("Stock for product id $productId, not found."))
                    }
                    else -> {
                        Mono.error(RuntimeException(response.statusCode().toString()))
                    }
                }
            }
            .bodyToMono(StockResponse::class.java)
    }
}

data class StockResponse(val productId: Int, val amountAvailable: Int)

@Configuration
class StockWebClientConfiguration {
    @Bean
    fun stockWebClient(
        @Value("\${external.ms.stock") stockUrl: String
    ): WebClient = WebClient.builder()
        .baseUrl(stockUrl)
        .clientConnector(getConnector())
        .codecs { configurer: ClientCodecConfigurer ->
            configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(jacksonObjectMapper()))
        }
        .build()

    private fun getConnector(): ClientHttpConnector {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected { conn: Connection ->
                conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }
        return ReactorClientHttpConnector(httpClient.wiretap(true))
    }

}