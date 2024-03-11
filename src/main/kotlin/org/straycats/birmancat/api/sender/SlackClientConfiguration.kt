package org.straycats.birmancat.api.sender

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.utils.RestClient

@Configuration
class SlackClientConfiguration(
    private val appEnvironment: AppEnvironment,
) {

    @Bean
    fun slackClient(): SlackClient {
        val env = appEnvironment.slack

        return if (env.useDummy) {
            DummySlackClient()
        } else {
            StableSlackClient(env, RestClient.new(env))
        }
    }
}
