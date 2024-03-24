package org.straycats.birmancat.api.domain.account.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.domain.account.AccountRegistrationInteraction
import org.straycats.birmancat.api.domain.sign.signin.PasswordChangeInteraction

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/maintenance/accounts")
class AccountMaintenanceController(
    private val accountRegistrationInteraction: AccountRegistrationInteraction,
    private val passwordChangeInteraction: PasswordChangeInteraction,
) {

    @Operation(summary = "Create Account")
    @PostMapping
    fun register(
        @RequestBody request: AccountResources.Request.TakeOverSignUp,
    ): Reply<Long> {
        return accountRegistrationInteraction.signUpByRandomPassword(request)
            .toReply()
    }

    @Operation(summary = "Reset Password")
    @PostMapping("{id}/reset-password")
    fun resetPassword(
        @PathVariable id: Long,
    ): Reply<Unit> {
        passwordChangeInteraction.reissue(id)
        return Unit.toReply()
    }
}
