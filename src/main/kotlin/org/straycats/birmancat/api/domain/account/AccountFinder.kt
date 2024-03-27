package org.straycats.birmancat.api.domain.account

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.straycats.birmancat.api.common.SearchCondition
import org.straycats.birmancat.api.config.DataNotFindException
import org.straycats.birmancat.api.config.DataNotSearchedException
import org.straycats.birmancat.api.domain.account.repository.AccountRepository
import org.straycats.birmancat.utils.and
import org.straycats.birmancat.utils.equal

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

    fun search(searchCondition: SearchCondition<Account>): List<Account> {
        if (searchCondition.isValid().not()) {
            throw DataNotSearchedException("invalid search condition")
        }

        return accountRepository.findAll(searchCondition.toSpecs())
    }

    internal class AccountSearchCondition(
        private val email: String?,
        private val phone: String?,
        private val department: String?,
        private val status: Account.Status?,
    ) : SearchCondition<Account> {
        override fun isValid(): Boolean = true

        override fun toSpecs(): Specification<Account> {
            return and(
                email?.let { Account::email.equal(it) },
                status?.let { Account::status.equal(it) },
                phone?.let { Account::phone.equal(it) },
                department?.let { Account::department.equal(it) },
            )
        }
    }
}
