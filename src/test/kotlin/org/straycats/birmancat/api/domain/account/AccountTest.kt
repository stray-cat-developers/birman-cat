package org.straycats.birmancat.api.domain.account

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.straycats.birmancat.api.config.PolicyException
import org.straycats.birmancat.utils.FixtureID
import org.straycats.birmancat.utils.invokeAny
import org.straycats.birmancat.utils.invokeId
import java.time.LocalDateTime

class AccountTest {

    @Test
    fun validateNotLoginTerm() {
        // Given
        val account = Account.aFixture("fixture@github.com")
        // When
        invokeAny(account, "lastAccess", LocalDateTime.now().minusDays(40))
        // Then

        assertThrows(PolicyException::class.java) {
            account.validateNotLoginTermOrThrow(10)
        }
    }
}

fun Account.Companion.aFixture(email: String): Account {
    return Account(
        email,
        "Fixture",
        Account.Role.NORMAL,
    ).apply {
        invokeId(this, FixtureID.inc(), true)
    }
}
