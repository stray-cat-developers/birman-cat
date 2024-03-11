package org.straycats.birmancat.utils

import java.util.concurrent.atomic.AtomicLong

object FixtureID {

    private val atomicLong = AtomicLong(1)
    private val atomicUserId = AtomicLong(1000)

    fun inc(): Long {
        return atomicLong.getAndIncrement()
    }

    fun userId() = atomicUserId.getAndIncrement()
}
