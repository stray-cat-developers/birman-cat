package org.straycats.birmancat.api.domain.account

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ChangePasswordTest {

    @Test
    fun create() {
        for (i in 1..10) {
            val plainPassword = Password.create(10)
            val valid = Password.validate(plainPassword)
            valid shouldBe true
        }
    }
}
