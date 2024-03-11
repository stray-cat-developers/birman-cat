package org.straycats.birmancat.utils

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.straycats.birmancat.api.config.AppEnvironment
import reactor.netty.http.client.HttpClient
import java.time.Duration

object RestWebClient {

    /**
     * WebClient Configuration
     * @ref https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html
     */
    fun new(connInfo: AppEnvironment.ConnInfo): WebClient {
        val exchangeStrategies = ExchangeStrategies.builder()
            .build().apply {
                messageWriters().stream()
                    .filter(LoggingCodecSupport::class::isInstance)
                    .forEach {
                            writer ->
                        writer as LoggingCodecSupport
                        writer.isEnableLoggingRequestDetails = connInfo.logging
                    }
            }

        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchangeStrategies(exchangeStrategies)
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connInfo.connTimeout)
                        .doOnConnected {
                            // Processing time of Netty Handler.
                            it.addHandlerLast(ReadTimeoutHandler(connInfo.connTimeout + connInfo.readTimeout.toInt() + 500))
                            // Processing time Http API
                        }.responseTimeout(Duration.ofMillis(connInfo.readTimeout)),
                ),
            ).build()
    }
}
