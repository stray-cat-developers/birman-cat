package org.straycats.birmancat.api.domain.account.api

import jakarta.servlet.http.Cookie
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.utils.fromJson
import org.straycats.birmancat.utils.toJson

class AccountControllerFlow(
    private val mockMvc: MockMvc,
    private val sessionCookie: Cookie,
) {
    private val mockSession = MockHttpSession()

    fun findMe(): AccountResources.Reply.Me {
        val uri = linkTo<AccountController> { findOne("", mockSession) }.toUri()
        return mockMvc.get(uri) {
            contentType = MediaType.APPLICATION_JSON
            cookie(sessionCookie)
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<AccountResources.Reply.Me>>()
            .content!!
    }

    fun modify(phone: String) {
        val request = AccountResources.Modify.SelfModify(
            "test",
            phone,
        )
        val uri = linkTo<AccountController> { modify("", mockSession, request) }.toUri()

        mockMvc.put(uri) {
            contentType = MediaType.APPLICATION_JSON
            cookie(sessionCookie)
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}
