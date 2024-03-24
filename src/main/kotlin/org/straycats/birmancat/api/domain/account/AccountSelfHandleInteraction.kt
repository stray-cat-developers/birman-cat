package org.straycats.birmancat.api.domain.account

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.domain.account.api.AccountResources
import org.straycats.birmancat.api.domain.account.repository.AccountRepository
import org.straycats.birmancat.api.domain.sign.signin.session.SignInSession
import org.straycats.birmancat.api.domain.sign.signup.NormalSignUp

@Service
@Transactional
class AccountSelfHandleInteraction(
    private val appEnvironment: AppEnvironment,
    private val accountRepository: AccountRepository,
    private val accountFinder: AccountFinder,
) {
    private val cryptoKey = appEnvironment.signIn.sessionCryptoKey
    fun signUp(request: AccountResources.Request.SignUp): Long {
        if (accountFinder.hasExistEmail(request.email)) {
            throw HumanException(NormalError(ErrorCode.HI00, "This email is already registered."))
        }

        val normalSignUp = NormalSignUp(request.email, request.name, request.password).apply {
            setAdditional(
                request.department,
                request.phone,
                request.description,
            )
        }

        normalSignUp.account.status = appEnvironment.signUp.defaultStatus

        return accountRepository.save(normalSignUp.account).id!!
    }

    fun findMe(): Account {
        return SignInSession(cryptoKey).getAttribute(accountFinder)
    }

    fun modify(modify: AccountResources.Modify.SelfModify) {
        val account = SignInSession(cryptoKey).getAttribute(accountFinder)

        account.apply {
            phone = modify.phone
            department = modify.department
        }

        accountRepository.save(account)
    }
}
