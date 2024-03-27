package org.straycats.birmancat.api.domain.account

import jakarta.transaction.Transactional
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.domain.account.api.AccountResources
import org.straycats.birmancat.api.domain.account.repository.AccountRepository
import org.straycats.birmancat.api.domain.sender.email.EmailSender
import org.straycats.birmancat.api.domain.sign.signup.DefaultPasswordSignUp

@Service
@Transactional
class AccountRegistrationInteraction(
    private val resourceLoader: ResourceLoader,
    private val appEnvironment: AppEnvironment,
    private val accountRepository: AccountRepository,
) {

    fun signUpByRandomPassword(request: AccountResources.Request.TakeOverSignUp): Long {
        val emailSender = EmailSender(resourceLoader, appEnvironment)

        val defaultPasswordSignUp = DefaultPasswordSignUp(
            request.email,
            request.name,
            request.role,
            emailSender,
        ).apply {
            setAdditional(
                request.department,
                request.phone,
                request.description,
            )
        }

        defaultPasswordSignUp.initPasswordAndSend()

        return accountRepository.save(defaultPasswordSignUp.account).id!!
    }
}
