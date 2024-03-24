package org.straycats.birmancat.api.domain.sign.signup.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.domain.account.AccountSelfHandleInteraction
import org.straycats.birmancat.api.domain.account.api.AccountResources

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/account")
class SignUpController(
    private val accountSelfHandleInteraction: AccountSelfHandleInteraction,
) {

    @Operation(summary = "sign up")
    @PostMapping
    fun signUp(
        @Valid @RequestBody
        request: AccountResources.Request.SignUp,
    ): Reply<Long> {
        return accountSelfHandleInteraction.signUp(request)
            .toReply()
    }
}
