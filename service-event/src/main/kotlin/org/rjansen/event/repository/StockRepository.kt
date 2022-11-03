package org.rjansen.event.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit


@Repository
class StockRepository(val stockWebClient: WebClient) {
    fun getStock(productId: Int): Int {
        stockWebClient.get().uri("/$productId").exchangeToMono { }
    }
}

@Configuration
class StockWebClientConfiguration {
    @Bean
    fun stockWebClient() = WebClient.builder()
        .baseUrl("http://localhost:8090/api/v1/stock")
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