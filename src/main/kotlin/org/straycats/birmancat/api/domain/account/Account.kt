package org.straycats.birmancat.api.domain.account

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.straycats.birmancat.api.common.Audit
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.common.NormalError
import org.straycats.birmancat.api.config.PolicyException
import org.straycats.birmancat.utils.Crypto
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
@Table(
    indexes = [
        Index(name = "IDX_EMAIL", columnList = "email"),
    ],
)
class Account(
    val email: String,
    val name: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
) : Audit() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    var department: String? = null
    var phone: String? = null
    var description: String? = null

    var password: String? = null
        private set
    var signInFailCount: Int = 0
        private set
    var passwordChangeDate: LocalDateTime? = null
        private set

    var lastAccess: LocalDateTime? = LocalDateTime.now()
        private set

    @Enumerated(EnumType.STRING)
    var status: Status = Status.ACTIVE

    fun setPassword(password: String) {
        this.password = Crypto.sha256(password)
        signInFailCount = 0
    }

    fun validateFailCountOrThrow(limitCount: Int) {
        if (signInFailCount >= limitCount) {
            throw PolicyException(NormalError(ErrorCode.FS01, "entered the wrong password repeatedly."))
        }
    }

    fun validateNotLoginTerm(day: Long) {
        val standard = LocalDateTime.now().minusDays(day)
        val calculated = ChronoUnit.DAYS.between(standard, lastAccess)

        if (calculated < 0) {
            throw PolicyException(NormalError(ErrorCode.FS01, "You haven't logged in for a while and need to change your PW."))
        }
    }

    fun successSignIn() {
        signInFailCount = 0
        lastAccess = LocalDateTime.now()
    }

    fun failSignIn() {
        signInFailCount += 1
    }

    enum class Status {
        ACTIVE,
        HOLD,
        EXPIRED,
    }

    enum class Role {
        SUPERVISOR,
        PRIVACY_PROTECTION,
        NORMAL,
    }

    companion object
}
