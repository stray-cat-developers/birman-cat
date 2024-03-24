package org.straycats.birmancat.api.domain.account.api

import org.junit.jupiter.api.Test
import org.straycats.birmancat.api.config.FlowTestSupport

class AccountMaintenanceControllerTest : FlowTestSupport() {

    @Test
    fun registerAndResetPassword() {
        val accountMaintenanceControllerFlow = AccountMaintenanceControllerFlow(mockMvc)
        val email = "sample-acc@github.com"

        val accountId = accountMaintenanceControllerFlow.registerWithOutPassword(email)

        accountMaintenanceControllerFlow.reissue(accountId)
    }
}
