package org.straycats.birmancat.api.domain.sign.signout.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.domain.sign.signout.SignOutInteraction

@Tag(name = "Account")
@RestController
@RequestMapping("/v1/account")
class SignOutController(
    private val signOutInteraction: SignOutInteraction,
) {

    @Operation(summary = "sign out")
    @DeleteMapping("sign-in")
    fun signOut(
        @Parameter(hidden = true)
        @CookieValue(name = "SESSION")
        sessionId: String,
        @Parameter(hidden = true)
        session: HttpSession,
    ): Reply<Unit> {
        signOutInteraction.signOut(sessionId)
        return Unit.toReply()
    }
}
