package org.straycats.birmancat.api.domain.account.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Replies
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReplies
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.AccountFinder
import org.straycats.birmancat.api.domain.account.AccountRegistrationInteraction
import org.straycats.birmancat.api.domain.sign.signin.PasswordChangeInteraction

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/maintenance/accounts")
class AccountMaintenanceController(
    private val accountRegistrationInteraction: AccountRegistrationInteraction,
    private val passwordChangeInteraction: PasswordChangeInteraction,
    private val accountFinder: AccountFinder,
    private val appEnvironment: AppEnvironment,
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

    @Operation(summary = "Find Account")
    @GetMapping("{id}")
    fun findOne(
        @PathVariable id: Long,
    ): Reply<AccountResources.Reply.MeWithDetail> {
        val limitCount = appEnvironment.signIn.limitFailCount
        val idleDay = appEnvironment.signIn.accountIdleDay

        val account = accountFinder.findById(id)
        return AccountResources.Reply.MeWithDetail.from(account, limitCount, idleDay)
            .toReply()
    }

    @Operation(summary = "Search Accounts")
    @GetMapping
    fun search(
        @RequestParam email: String?,
        @RequestParam phone: String?,
        @RequestParam department: String?,
        @RequestParam status: Account.Status?,
    ): Replies<AccountResources.Reply.Me> {
        val condition = AccountFinder.AccountSearchCondition(email, phone, department, status)
        return accountFinder.search(condition)
            .map { AccountResources.Reply.Me.from(it) }
            .toReplies()
    }
}
