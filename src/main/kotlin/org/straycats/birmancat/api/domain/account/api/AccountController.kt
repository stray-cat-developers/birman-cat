package org.straycats.birmancat.api.domain.account.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.domain.account.AccountSelfHandleInteraction

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/account")
class AccountController(
    private val accountSelfHandleInteraction: AccountSelfHandleInteraction,
) {
    @Operation(summary = "Get Account (Session Attribute)")
    @GetMapping("/me")
    fun findOne(
        @Parameter(hidden = true)
        @CookieValue(name = "SESSION")
        sessionId: String,
        @Parameter(hidden = true)
        session: HttpSession,
    ): Reply<AccountResources.Reply.Me> {
        val account = accountSelfHandleInteraction.findMe()

        return AccountResources.Reply.Me.from(account)
            .toReply()
    }

    @Operation(summary = "self modify account")
    @PutMapping("/me")
    fun modify(
        @Parameter(hidden = true)
        @CookieValue(name = "SESSION")
        sessionId: String,
        @Parameter(hidden = true)
        session: HttpSession,
        @RequestBody request: AccountResources.Modify.SelfModify,
    ): Reply<Unit> {
        accountSelfHandleInteraction.modify(request)
        return Unit.toReply()
    }

    @Operation(summary = "refresh session")
    @PostMapping("/refresh-session")
    fun refresh(
        @Parameter(hidden = true)
        @CookieValue(name = "SESSION")
        sessionId: String,
        @Parameter(hidden = true)
        session: HttpSession,
    ): Reply<Unit> {
        return Unit.toReply()
    }
}
