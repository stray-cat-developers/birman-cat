package org.straycats.birmancat.api.domain.account.api

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
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

    class Modify {
        data class SelfModify(
            var department: String?,
            var phone: String?,
        )
    }

    class Reply {
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
    }
}
