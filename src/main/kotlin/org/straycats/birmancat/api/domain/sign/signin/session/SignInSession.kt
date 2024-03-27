package org.straycats.birmancat.api.domain.sign.signin.session

import jakarta.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.AccountFinder
import org.straycats.birmancat.utils.Crypto

/**
 * If a session does not exist, create it.
 */
class SignInSession {
    private val cryptoKey: String
    private val httpSession: HttpSession

    constructor(cryptoKey: String) {
        this.cryptoKey = cryptoKey
        this.httpSession = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.request.session
    }

    constructor(cryptoKey: String, httpSession: HttpSession) {
        this.cryptoKey = cryptoKey
        this.httpSession = httpSession
    }

    fun isSignIn(): Boolean {
        val result = runCatching {
            require(httpSession.getAttribute(ACCOUNT_TOKEN) != null)
        }
        return result.isSuccess
    }

    fun getAttribute(accountFinder: AccountFinder): Account {
        if (isSignIn().not()) {
            throw HumanException(NormalError(ErrorCode.FS02, "Please sign in"))
        }

        val token = httpSession.getAttribute(ACCOUNT_TOKEN).toString()
        val payload = Payload.from(cryptoKey, token)
        val account = accountFinder.findByEmail(payload.email)

        if (account.id!! != payload.id) {
            throw HumanException(NormalError(ErrorCode.HA00))
        }

        return account
    }

    fun getAccountPayload(): Payload {
        if (isSignIn().not()) {
            throw HumanException(NormalError(ErrorCode.FS02, "Please sign in"))
        }

        val token = httpSession.getAttribute(ACCOUNT_TOKEN).toString()
        return Payload.from(cryptoKey, token)
    }

    fun setAttribute(account: Account) {
        val payload = account.run {
            Payload(id!!, email, name, role)
        }
        val token = payload.toToken(cryptoKey)

        httpSession.setAttribute(ACCOUNT_TOKEN, token)
        httpSession.setAttribute(ACCOUNT_EMAIL, account.email)
    }

    data class Payload(
        val id: Long,
        val email: String,
        val name: String,
        val role: Account.Role,
    ) {
        fun toToken(cryptoKey: String): String {
            return Crypto(cryptoKey).enc(this)
        }

        companion object {
            fun from(cryptoKey: String, token: String): Payload {
                return Crypto(cryptoKey).dec(token, Payload::class.java)
            }
        }
    }

    companion object {
        const val ACCOUNT_TOKEN = "accountToken"
        const val ACCOUNT_EMAIL = "accountEmail"
    }
}
