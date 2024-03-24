package org.straycats.birmancat.api.domain.sign.signup

import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.Password

class NormalSignUp(email: String, name: String, password: String) : SignUp {

    override val account: Account

    init {
        Password.validOrThrow(password)
        this.account = Account(email, name, Account.Role.NORMAL).apply {
            setPassword(password)
        }
    }
}
