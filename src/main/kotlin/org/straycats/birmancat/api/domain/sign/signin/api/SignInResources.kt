package org.straycats.birmancat.api.domain.sign.signin.api

import jakarta.validation.constraints.Email

class SignInResources {

    class Request {
        data class SignIn(
            @Email
            val email: String,
            val password: String,
        )

        data class ChangePassword(
            @Email
            val email: String,
            val beforePassword: String,
            val afterPassword: String,
        )
    }
}
