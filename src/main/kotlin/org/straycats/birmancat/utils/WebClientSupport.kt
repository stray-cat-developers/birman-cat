package org.straycats.birmancat.utils

import org.slf4j.Logger
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

open class WebClientSupport(
    private val writeLog: Boolean,
    val log: Logger,
) {
    fun WebClient.post(
        url: String,
        headers: Map<String, String>,
        body: Any,
    ): WebClient.ResponseSpec {
        return this.post()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .bodyValue(body)
            .retrieve()
    }

    fun WebClient.post(
        url: String,
        headers: Map<String, String>,
    ): WebClient.ResponseSpec {
        return this.post()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }

    fun WebClient.post(
        url: String,
        headers: Map<String, String>,
        params: Map<String, String?>? = null,
    ): WebClient.ResponseSpec {
        val uri = UriComponentsBuilder.fromPath(url)
            .apply {
                if (params.isNullOrEmpty().not()) {
                    this.queryParams(
                        LinkedMultiValueMap<String, String>().apply {
                            setAll(params!!.filterValues { it.isNullOrEmpty().not() })
                        },
                    )
                }
            }.build()
            .toUriString()

        return this.post()
            .uri(uri)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }

    fun WebClient.put(
        url: String,
        headers: Map<String, String>,
        body: Any,
    ): WebClient.ResponseSpec {
        return this.put()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .bodyValue(body)
            .retrieve()
    }

    fun WebClient.put(
        url: String,
        headers: Map<String, String>,
    ): WebClient.ResponseSpec {
        return this.put()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }

    fun WebClient.patch(
        url: String,
        headers: Map<String, String>,
        body: Any,
    ): WebClient.ResponseSpec {
        return this.patch()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }.bodyValue(body)
            .retrieve()
    }

    fun WebClient.patch(
        url: String,
        headers: Map<String, String>,
    ): WebClient.ResponseSpec {
        return this.patch()
            .uri(url)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }

    fun WebClient.delete(
        url: String,
        headers: Map<String, String>,
        params: Map<String, String?>? = null,
    ): WebClient.ResponseSpec {
        val uri = UriComponentsBuilder.fromPath(url)
            .apply {
                if (params.isNullOrEmpty().not()) {
                    this.queryParams(
                        LinkedMultiValueMap<String, String>().apply {
                            setAll(params!!.filterValues { it.isNullOrEmpty().not() })
                        },
                    )
                }
            }.build()
            .toUriString()

        return this.delete()
            .uri(uri)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }

    fun WebClient.get(
        url: String,
        headers: Map<String, String>,
        params: Map<String, String?>? = null,
    ): WebClient.ResponseSpec {
        val uri = UriComponentsBuilder.fromPath(url)
            .apply {
                if (params.isNullOrEmpty().not()) {
                    this.queryParams(
                        LinkedMultiValueMap<String, String>().apply {
                            setAll(params!!.filterValues { it.isNullOrEmpty().not() })
                        },
                    )
                }
            }.build()
            .toUriString()

        return this.get()
            .uri(uri)
            .headers { httpHeaders ->
                headers.forEach { httpHeaders.set(it.key, it.value) }
            }
            .retrieve()
    }
}
