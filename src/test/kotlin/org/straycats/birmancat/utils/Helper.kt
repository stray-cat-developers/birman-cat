package org.straycats.birmancat.utils

fun String.Companion.random(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..length).map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
