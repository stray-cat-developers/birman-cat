package org.straycats.birmancat.api.domain.sign.signin

import org.slf4j.LoggerFactory
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.Password

class PWSignIn(
    private val account: Account,
    private val environment: AppEnvironment,
) {
    fun validateOrThrow() {
        when (account.status) {
            Account.Status.HOLD -> throw HumanException(NormalError(ErrorCode.PL02, "Your account has been temporarily disabled."))
            Account.Status.EXPIRED -> throw HumanException(NormalError(ErrorCode.HD00))
            Account.Status.ACTIVE -> {}
        }

        account.validateFailCountOrThrow(environment.signIn.limitFailCount)
        account.validateNotLoginTermOrThrow(environment.signIn.accountIdleDay)
    }

    fun match(password: String): Boolean {
        return if (Password.match(account, password)) {
            account.successSignIn()
            true
        } else {
            account.failSignIn()
            false
        }
    }
}
