package org.straycats.birmancat.api.domain.account

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.straycats.birmancat.api.config.DataNotFindException
import org.straycats.birmancat.api.domain.account.repository.AccountRepository

@Service
@Transactional(readOnly = true)
class AccountFinder(
    private val accountRepository: AccountRepository,
) {

    fun findById(id: Long): Account {
        return accountRepository.findByIdOrNull(id) ?: throw DataNotFindException("no data registered with that ID.")
    }

    fun findByEmail(email: String): Account {
        val accounts = accountRepository.findAllByEmailAndStatusIn(email, listOf(Account.Status.ACTIVE, Account.Status.HOLD)) ?: emptyList()
        if (accounts.isEmpty()) {
            throw DataNotFindException("account not found")
        }
        if (accounts.size != 1) {
            throw IllegalStateException("Contact your system administrator.")
        }
        return accounts.first()
    }

    fun hasExistEmail(email: String): Boolean {
        val accounts = accountRepository.findAllByEmailAndStatusIn(email, listOf(Account.Status.ACTIVE, Account.Status.HOLD)) ?: emptyList()
        return accounts.isNotEmpty()
    }
}
