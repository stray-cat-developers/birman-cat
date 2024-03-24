package org.straycats.birmancat.api.domain.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.straycats.birmancat.api.domain.account.Account

interface AccountRepository : JpaRepository<Account, Long> {

    fun findAllByEmailAndStatusIn(email: String, status: List<Account.Status>): List<Account>?
}
