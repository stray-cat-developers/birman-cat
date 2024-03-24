package org.straycats.birmancat.api.domain.sign.signup.api

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.domain.account.api.AccountResources
import org.straycats.birmancat.utils.fromJson
import org.straycats.birmancat.utils.toJson

class SignUpControllerFlow(
    private val mockMvc: MockMvc,
) {

    fun signUp(email: String, password: String): Long {
        val request = AccountResources.Request.SignUp(
            email,
            password,
            "SIGN_UP_TEST",
            "sample",
            "010-1234-1234",
            "test",
        )
        val uri = linkTo<SignUpController> { signUp(request) }.toUri()

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
}
