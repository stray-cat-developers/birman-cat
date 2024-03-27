package org.straycats.birmancat.api.permission

import org.springframework.http.HttpMethod
import org.straycats.birmancat.api.domain.account.Account

data class AuthorizationRule(
    val pattern: String,
    val httpMethods: List<HttpMethod>? = null,
    val roles: List<Account.Role>? = null,
)
