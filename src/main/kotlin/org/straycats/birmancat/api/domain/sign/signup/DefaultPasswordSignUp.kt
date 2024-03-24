package org.straycats.birmancat.api.domain.sign.signup

import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.Password
import org.straycats.birmancat.api.domain.sender.Sender

class DefaultPasswordSignUp(
    email: String,
    name: String,
    role: Account.Role,
    private val sender: Sender,
) : SignUp {
    override val account = Account(email, name, role)

    fun initPasswordAndSend() {
        val password = Password.create(10)
        account.setPassword(password)

        sender.init(account.email)
        val payload = mapOf(
            "name" to account.name,
            "password" to password,
            "template" to Sender.Template.DEFAULT_PASSWORD.name,
        )
        sender.setPayload(payload)
        sender.send()
    }
}
