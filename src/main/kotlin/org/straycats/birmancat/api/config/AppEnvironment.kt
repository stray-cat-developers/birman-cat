package org.straycats.birmancat.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppEnvironment {
    val slack = Slack()
    class Slack : ConnInfo() {
        lateinit var token: String
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
