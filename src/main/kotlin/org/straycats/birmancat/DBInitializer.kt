package org.straycats.birmancat

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.repository.AccountRepository

@Profile("default")
@Configuration
@Transactional
class DBInitializer(
    private val accountRepository: AccountRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        addSupervisorAccount(SUPERVISOR_ACCOUNT)
    }

    private fun addSupervisorAccount(email: String) {
        val account = Account(
            email,
            "supervisor",
            Account.Role.SUPERVISOR,
        ).apply {
            setPassword(ACCOUNT_PASSWORD)
        }

        accountRepository.save(account)
    }

    companion object {
        const val SUPERVISOR_ACCOUNT = "LOCAL_SUPERVISOR@github.com"
        const val ACCOUNT_PASSWORD = "Madcat1234!"
    }
}
