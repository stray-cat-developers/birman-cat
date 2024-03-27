package org.straycats.birmancat.api.domain.account.api

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.straycats.birmancat.TestDBInitializer
import org.straycats.birmancat.api.config.FlowTestSupport
import org.straycats.birmancat.api.domain.sign.signin.api.SignInControllerFlow

class AccountControllerTest : FlowTestSupport() {

    @Test
    fun findOne() {
        val signInControllerFlow = SignInControllerFlow(mockMvc)
        val cookie = signInControllerFlow.signIn(TestDBInitializer.PRIVACY_PROTECTION_ACCOUNT, TestDBInitializer.ACCOUNT_PASSWORD)
        val accountControllerFlow = AccountControllerFlow(mockMvc, cookie[0])

        val account = accountControllerFlow.findMe()
        account.email shouldBe TestDBInitializer.PRIVACY_PROTECTION_ACCOUNT
    }

    @Test
    fun modify() {
        val signInControllerFlow = SignInControllerFlow(mockMvc)
        val cookie = signInControllerFlow.signIn(TestDBInitializer.PRIVACY_PROTECTION_ACCOUNT, TestDBInitializer.ACCOUNT_PASSWORD)
        val accountControllerFlow = AccountControllerFlow(mockMvc, cookie[0])

        accountControllerFlow.modify("010-1234-1234")
    }
}
