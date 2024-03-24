package org.straycats.birmancat.api.domain.sign.signin

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.config.PolicyException
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.AccountFinder
import org.straycats.birmancat.api.domain.account.Password
import org.straycats.birmancat.api.domain.account.repository.AccountRepository
import org.straycats.birmancat.api.domain.sender.Sender
import org.straycats.birmancat.api.domain.sender.email.EmailSender
import org.straycats.birmancat.api.domain.sign.signin.api.SignInResources

@Service
class PasswordChangeInteraction(
    private val accountFinder: AccountFinder,
    private val appEnvironment: AppEnvironment,
    private val accountRepository: AccountRepository,
    private val resourceLoader: ResourceLoader,
) {

    fun change(request: SignInResources.Request.ChangePassword) {
        Password.validOrThrow(request.afterPassword)

        val account = accountFinder.findByEmail(request.email)

        if (Password.match(account, request.beforePassword).not()) {
            throw HumanException(NormalError(ErrorCode.HA02, "Please recheck your email and password."))
        }

        account.setPassword(request.afterPassword)

        accountRepository.save(account)
    }

    fun reissue(id: Long) {
        val account = accountFinder.findById(id)

        if (account.status == Account.Status.EXPIRED) {
            throw PolicyException(NormalError(ErrorCode.HD01))
        }

        val password = Password.create(10)
        account.setPassword(password)

        accountRepository.save(account)

        run {
            EmailSender(resourceLoader, appEnvironment).apply {
                init(account.email)
                val payload = mapOf(
                    "name" to account.name,
                    "password" to password,
                    "template" to Sender.Template.RESET_PASSWORD.name,
                )
                setPayload(payload)
                send()
            }
        }
    }
}
