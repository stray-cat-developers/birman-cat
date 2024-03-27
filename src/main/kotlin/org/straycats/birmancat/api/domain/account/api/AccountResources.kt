package org.straycats.birmancat.api.domain.account.api

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import org.straycats.birmancat.api.config.PolicyException
import org.straycats.birmancat.api.domain.account.Account
import java.time.LocalDateTime

class AccountResources {

    class Request {

        @Schema(name = "BirmanCat.Account.Request.TakeOverSignUp")
        data class TakeOverSignUp(
            @Email
            val email: String,
            val name: String,
            val role: Account.Role,
            var department: String?,
            var phone: String?,
            var description: String?,
        )

        @Schema(name = "BirmanCat.Account.Request.SignUp")
        data class SignUp(
            @Email
            val email: String,
            val password: String,
            val name: String,
            var department: String?,
            var phone: String?,
            var description: String?,
        )
    }

    @Schema(name = "BirmanCat.Account.Modify.SelfModify")
    class Modify {
        data class SelfModify(
            var department: String?,
            var phone: String?,
        )
    }

    class Reply {
        @Schema(name = "BirmanCat.Account.Me")
        data class Me(
            val id: Long,
            val email: String,
            val name: String,
            val role: Account.Role,
            val department: String?,
            val phone: String?,
            val description: String?,
            val lastAccess: LocalDateTime?,
            val status: Account.Status,
        ) {
            companion object {
                fun from(account: Account): Me {
                    return account.run {
                        Me(id!!, email, name, role, department, phone, description, lastAccess, status)
                    }
                }
            }
        }

        @Schema(name = "BirmanCat.Account.MeWithDetail")
        data class MeWithDetail(
            val id: Long,
            val email: String,
            val name: String,
            val role: Account.Role,
            val department: String?,
            val phone: String?,
            val description: String?,
            val lastAccess: LocalDateTime?,
            val status: Account.Status,
            val signInFailCount: Int,
            val passwordChangeDate: LocalDateTime?,
            val lotOfWrongPassword: Boolean,
            val notSignInTooLong: Boolean,
        ) {
            companion object {
                fun from(account: Account, passwordWrongLimitCount: Int, availableDay: Long): MeWithDetail {
                    return account.run {
                        val lotOfWrongPassword = try {
                            validateFailCountOrThrow(passwordWrongLimitCount)
                            false
                        } catch (e: PolicyException) { true }

                        val notSignInTooLong = try {
                            validateNotLoginTermOrThrow(availableDay)
                            false
                        } catch (e: PolicyException) { true }

                        MeWithDetail(
                            id!!, email, name, role, department, phone, description, lastAccess, status,
                            signInFailCount,
                            passwordChangeDate,
                            lotOfWrongPassword,
                            notSignInTooLong,
                        )
                    }
                }
            }
        }
    }
}
