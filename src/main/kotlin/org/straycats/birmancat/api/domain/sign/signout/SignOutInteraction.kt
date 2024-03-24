package org.straycats.birmancat.api.domain.sign.signout

import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Service
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.domain.sign.signin.session.SignInSession

@Service
class SignOutInteraction(
    appEnvironment: AppEnvironment,
    private val indexedSessionRepository: RedisIndexedSessionRepository,
) {
    private val cryptoKey = appEnvironment.signIn.sessionCryptoKey

    fun signOut(sessionId: String) {
        if (SignInSession(cryptoKey).isSignIn()) {
            indexedSessionRepository.deleteById(sessionId)
        }
    }
}
