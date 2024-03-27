package org.straycats.birmancat.api.domain.sign.signin.api

import jakarta.servlet.http.Cookie
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.straycats.birmancat.utils.toJson

class SignInControllerFlow(
    private val mockMvc: MockMvc,
) {

    fun signIn(email: String, password: String): Array<Cookie> {
        val request = SignInResources.Request.SignIn(
            email,
            password,
        )
        val uri = linkTo<SignInController> { signIn(request) }.toUri()

        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .cookies
    }

    fun changePassword(request: SignInResources.Request.ChangePassword) {
        val uri = linkTo<SignInController> { changePassword(request) }.toUri()

        mockMvc.put(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}
