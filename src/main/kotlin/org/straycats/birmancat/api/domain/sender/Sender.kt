package org.straycats.birmancat.api.domain.sender

interface Sender {

    fun init(from: String, to: String)
    fun init(to: String)

    fun setPayload(payloadMap: Map<String, Any?>)

    fun send(): Boolean

    enum class Template {
        DEFAULT_PASSWORD,
        RESET_PASSWORD,
    }
}
