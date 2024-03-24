package org.straycats.birmancat.api.domain.sign.signin

import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Service
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.AccountFinder
import org.straycats.birmancat.api.domain.account.repository.AccountRepository
import org.straycats.birmancat.api.domain.sign.signin.api.SignInResources
import org.straycats.birmancat.api.domain.sign.signin.session.SignInSession

@Service
class SignInInteraction(
    private val accountFinder: AccountFinder,
    private val redisIndexedSessionRepository: RedisIndexedSessionRepository,
    private val appEnvironment: AppEnvironment,
    private val accountRepository: AccountRepository,
) {
    private val cryptoKey = appEnvironment.signIn.sessionCryptoKey

    fun signIn(
        request: SignInResources.Request.SignIn,

    ): Account {
        val account = accountFinder.findByEmail(request.email)
        val pwSignIn = PWSignIn(account, appEnvironment).apply {
            validateOrThrow()
        }

        val isMatch = pwSignIn.match(request.password)
        accountRepository.save(account)

        if (isMatch.not()) {
            throw HumanException(NormalError(ErrorCode.HA02, "Login failed. Please recheck your email and password."))
        }

        SignInSession(cryptoKey).setAttribute(account)
        return account
    }

    /**
     * Suppress("INACCESSIBLE_TYPE")
     * @ref: https://www.baeldung.com/kotlin/public-function-exposes-its-public-package-return-type
     */
    @Suppress("INACCESSIBLE_TYPE")
    fun removeDuplicatedSignIn(email: String) {
        val redisSessionMap = redisIndexedSessionRepository.findByPrincipalName(email)

        redisSessionMap.keys.forEach {
            redisIndexedSessionRepository.deleteById(it)
        }
    }
}
