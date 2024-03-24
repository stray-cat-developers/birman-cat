package org.straycats.birmancat.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.straycats.birmancat.api.domain.sign.signin.session.SignInSession
import java.util.Optional

@Configuration
@EnableJpaAuditing
class AuditConfiguration {

    @Bean
    internal fun auditorAware(
        appEnvironment: AppEnvironment,
    ): AuditorAware<*> {
        val cryptoKey = appEnvironment.signIn.sessionCryptoKey
        return AuditorAwareImpl(cryptoKey)
    }
}

class AuditorAwareImpl(
    private val sessionCryptoKey: String,
) : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        var auditor = "S:unknown"

        if (RequestContextHolder.getRequestAttributes() != null) {
            val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

            if (request.getSession(false) == null) {
                return Optional.of("S:no-session")
            }

            val signInSession = SignInSession(sessionCryptoKey)

            if (signInSession.isSignIn().not()) {
                return Optional.of("S:no-sign")
            }

            auditor = signInSession.getAccountPayload().id.toString()
        }
        return Optional.of("A:$auditor")
    }
}
