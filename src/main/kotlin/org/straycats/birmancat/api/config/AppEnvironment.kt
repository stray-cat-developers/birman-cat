package org.straycats.birmancat.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.straycats.birmancat.api.domain.account.Account

@ConfigurationProperties(prefix = "app")
class AppEnvironment {
    val slack = Slack()
    val signIn = SignIn()
    val signUp = SignUp()
    val corporation = Corporation()
    class Slack : ConnInfo() {
        lateinit var token: String
    }

    class Corporation {
        var notifyEmail: String = "birman-cat@github.com"
    }

    class SignUp {
        var defaultStatus: Account.Status = Account.Status.ACTIVE
    }
    class SignIn {
        var limitFailCount: Int = 5
        var accountIdleDay: Long = 30
        var sessionCryptoKey: String = ""
    }

    open class ConnInfo {
        var host: String = "localhost"
        var connTimeout: Int = 1000
        var readTimeout: Long = 2000
        var logging: Boolean = false
        var useDummy: Boolean = false
        var perRoute: Int = 50
        var connTotal: Int = 500
        var connLiveDuration: Long = 60
    }
}
