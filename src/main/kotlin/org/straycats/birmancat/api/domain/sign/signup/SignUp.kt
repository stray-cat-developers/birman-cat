package org.straycats.birmancat.api.domain.sign.signup

import org.straycats.birmancat.api.domain.account.Account

interface SignUp {
    val account: Account

    fun setAdditional(
        department: String?,
        phone: String?,
        description: String?,
    ) {
        account.department = department
        account.phone = phone
        account.description = description
    }
}
