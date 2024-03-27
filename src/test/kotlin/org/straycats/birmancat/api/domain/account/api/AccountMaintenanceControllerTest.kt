package org.straycats.birmancat.api.domain.account.api

import io.kotest.matchers.longs.shouldBeGreaterThan
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.straycats.birmancat.TestDBInitializer
import org.straycats.birmancat.api.config.FlowTestSupport
import org.straycats.birmancat.api.domain.sign.signin.api.SignInControllerFlow

class AccountMaintenanceControllerTest : FlowTestSupport() {

    @Test
    fun registerAndResetPassword() {
        val signInControllerFlow = SignInControllerFlow(mockMvc)
        val cookie = signInControllerFlow.signIn(TestDBInitializer.SUPERVISOR_ACCOUNT, TestDBInitializer.ACCOUNT_PASSWORD)
        val accountMaintenanceControllerFlow = AccountMaintenanceControllerFlow(mockMvc, cookie[0])

        val email = "registerAndResetPassword@github.com"
        val accountId = accountMaintenanceControllerFlow.registerWithOutPassword(email)

        accountId shouldBeGreaterThan 0

        accountMaintenanceControllerFlow.reissue(accountId)
    }

    @Test
    fun unAuthorizationRole() {
        val signInControllerFlow = SignInControllerFlow(mockMvc)
        val cookie = signInControllerFlow.signIn(TestDBInitializer.PRIVACY_PROTECTION_ACCOUNT, TestDBInitializer.ACCOUNT_PASSWORD)
        val accountMaintenanceControllerFlow = AccountMaintenanceControllerFlow(mockMvc, cookie[0])

        val email = "unAuthorizationRole@github.com"
        assertThrows(AssertionError::class.java) {
            accountMaintenanceControllerFlow.registerWithOutPassword(email)
        }
    }
}
