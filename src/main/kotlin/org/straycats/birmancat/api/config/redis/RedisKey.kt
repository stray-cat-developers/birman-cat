package org.straycats.birmancat.api.config.redis

interface RedisKey {
    fun getKey(): String

    companion object {
        const val PREFIX = "birman-cat"
    }
}
