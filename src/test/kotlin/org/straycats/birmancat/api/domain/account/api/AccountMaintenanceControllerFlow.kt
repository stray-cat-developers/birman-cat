package org.straycats.birmancat.api.domain.account.api

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.utils.fromJson
import org.straycats.birmancat.utils.toJson

class AccountMaintenanceControllerFlow(
    private val mockMvc: MockMvc,
) {

    fun registerWithOutPassword(email: String): Long {
        val request = AccountResources.Request.TakeOverSignUp(
            email,
            "TEST",
            Account.Role.PRIVACY_PROTECTION,
            "Sample",
            "01012341234",
            "Flow Test",
        )
        val uri = linkTo<AccountMaintenanceController> { register(request) }.toUri()

        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<Long>>()
            .content!!
    }

    fun reissue(id: Long) {
        val uri = linkTo<AccountMaintenanceController> { resetPassword(id) }.toUri()

        mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}
