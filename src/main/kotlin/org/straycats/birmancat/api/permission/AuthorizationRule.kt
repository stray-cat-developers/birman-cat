package org.straycats.birmancat.api.permission

import org.springframework.http.HttpMethod
import org.straycats.birmancat.api.domain.account.Account

class AuthorizationRule(
    private val pattern: String,
    private val httpMethod: HttpMethod? = null,
    private val servletPath: String? = null,
    private val rules: List<Account.Role>,
)
