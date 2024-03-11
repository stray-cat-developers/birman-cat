package org.straycats.birmancat.api.common

import com.fasterxml.jackson.annotation.JsonUnwrapped
open class Reply<T>() {
    @get:JsonUnwrapped
    var content: T? = null

    constructor(content: T) : this() {
        this.content = content
    }

    override fun toString(): String {
        return String.format("Resource { content: %s, %s }", content, super.toString())
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result += if (content == null) 0 else 17 * content!!.hashCode()
        return result
    }
}

fun <T> T.toReply(): Reply<T> = Reply(this)
