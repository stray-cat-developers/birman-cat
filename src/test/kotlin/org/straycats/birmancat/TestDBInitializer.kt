package org.straycats.birmancat

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.repository.AccountRepository

@Profile("embedded")
@Configuration
@Transactional
class TestDBInitializer(
    private val accountRepository: AccountRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        addAccount(SUPERVISOR_ACCOUNT, Account.Role.SUPERVISOR)
        addAccount(PRIVACY_PROTECTION_ACCOUNT, Account.Role.PRIVACY_PROTECTION)
    }

    private fun addAccount(email: String, role: Account.Role) {
        val account = Account(
            email,
            "admin",
            role,
        ).apply {
            setPassword(ACCOUNT_PASSWORD)
        }

        accountRepository.save(account)
    }

    companion object {
        const val SUPERVISOR_ACCOUNT = "SUPERVISOR@github.com"
        const val PRIVACY_PROTECTION_ACCOUNT = "PROTECTION@github.com"
        const val ACCOUNT_PASSWORD = "Madcat1234!"
    }
}
