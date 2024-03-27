package org.straycats.birmancat.api.permission

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.hc.core5.http.ContentType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.server.PathContainer
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.config.GlobalErrorFormat
import org.straycats.birmancat.api.config.HumanException
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.sign.signin.session.SignInSession
import org.straycats.birmancat.utils.toJson
import java.time.LocalDateTime

class AuthorizationFilter(
    private val sessionCryptoKey: String,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val uri = request.requestURI
        val httpMethod = HttpMethod.valueOf(request.method)
        val rule = findRule(uri)

        if (rule == null) {
            filterChain.doFilter(request, response)
        } else {
            val matchedHttpMethod = matchMethod(rule, httpMethod)
            val matchedRole = matchRole(rule, request)

            if (matchedHttpMethod && matchedRole) {
                filterChain.doFilter(request, response)
            } else {
                val json = getFatalBody(matchedHttpMethod, matchedRole)
                response.run {
                    status = 400
                    contentType = ContentType.APPLICATION_JSON.mimeType
                    characterEncoding = ContentType.APPLICATION_JSON.charset.name()
                    writer.write(json)
                }
            }
        }
    }

    private fun getFatalBody(matchedHttpMethod: Boolean, matchedRole: Boolean): String {
        return GlobalErrorFormat(
            LocalDateTime.now().toString(),
            ErrorCode.HA03.name,
            "miss match rule. method is $matchedHttpMethod, role is $matchedRole",
            "Unauthorized Permission",
            "UnAuthorizedException",
        ).toJson()
    }

    private fun findRule(uri: String): AuthorizationRule? {
        val pathPatternParser = PathPatternParser()
        for (rule in AUTHORIZE_URLS) {
            val pathPattern = pathPatternParser.parse(rule.pattern)
            if (matchPath(pathPattern, uri)) {
                return rule
            }
        }
        return null
    }

    private fun matchPath(pathPattern: PathPattern, uri: String): Boolean {
        val path = PathContainer.parsePath(uri)
        return pathPattern.matches(path)
    }

    private fun matchMethod(rule: AuthorizationRule, httpMethod: HttpMethod): Boolean {
        if (rule.httpMethods.isNullOrEmpty()) {
            return true
        }

        val hasMatch = rule.httpMethods.find { it == httpMethod }

        return (hasMatch != null)
    }

    private fun matchRole(rule: AuthorizationRule, request: HttpServletRequest): Boolean {
        if (rule.roles.isNullOrEmpty()) {
            return true
        }

        val session = request.getSession(false) ?: return false

        try {
            val sessionRole = SignInSession(sessionCryptoKey, session).getAccountPayload().role
            val hasRole = rule.roles.find { it == sessionRole }

            return (hasRole != null)
        } catch (e: HumanException) {
            log.warn("No SignIn", e)
            return false
        }
    }

    companion object {
        val AUTHORIZE_URLS = listOf(
            AuthorizationRule("/v1/maintenance/*", roles = listOf(Account.Role.SUPERVISOR)),
        )
    }
}
