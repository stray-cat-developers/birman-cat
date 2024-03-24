package org.straycats.birmancat.api.domain.account

import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.utils.Crypto

object Password {

    fun match(account: Account, password: String): Boolean {
        return account.password == Crypto.sha256(password)
    }

    fun create(length: Int): String {
        val lowerPool = ('a'..'z').toList()
        val upperPool = ('A'..'Z').toList()
        val numberPool = ('0'..'9').toList()
        val specialPool = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')')

        return (1..length).map { num ->
            when (num % 4) {
                0 -> lowerPool
                1 -> upperPool
                2 -> numberPool
                3 -> specialPool
                else -> lowerPool
            }.random()
        }.shuffled()
            .joinToString("")
    }

    fun validate(password: String): Boolean {
        val result = runCatching {
            require(password.length >= 8)
            require(password.none { it.isWhitespace() })
            require(password.any { it.isDigit() })
            require(password.any { it.isUpperCase() })
            require(password.any { !it.isLetterOrDigit() })
        }

        return result.isSuccess
    }

    fun validOrThrow(password: String) {
        if (this.validate(password).not()) {
            throw HumanException(NormalError(ErrorCode.P002, "The password does not meet the creation rules."))
        }
    }
}
