package org.straycats.birmancat.api.domain.sign.signin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.domain.sign.signin.PasswordChangeInteraction
import org.straycats.birmancat.api.domain.sign.signin.SignInInteraction

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/account")
class SignInController(
    private val signInInteraction: SignInInteraction,
    private val passwordChangeInteraction: PasswordChangeInteraction,
) {

    @Operation(summary = "sign in")
    @PostMapping("sign-in")
    fun signIn(
        @Valid @RequestBody
        request: SignInResources.Request.SignIn,
    ): Reply<Unit> {
        signInInteraction.removeDuplicatedSignIn(request.email)
        signInInteraction.signIn(request)

        return Unit.toReply()
    }

    @Operation(summary = "change password")
    @PutMapping
    fun changePassword(
        @Valid @RequestBody
        request: SignInResources.Request.ChangePassword,
    ): Reply<Unit> {
        passwordChangeInteraction.change(request)
        return Unit.toReply()
    }
}
