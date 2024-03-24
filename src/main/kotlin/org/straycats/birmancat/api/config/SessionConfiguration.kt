package org.straycats.birmancat.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession
import org.straycats.birmancat.utils.Jackson

/***
 * This application is for admins. the load on Redis is expected to be low.
 * Therefore, instead of using 'RedisHttpSession', use 'EnableRedisIndexedHttpSession' to accurately know the current Session status.
 *
 */
@Configuration
@EnableRedisIndexedHttpSession(
    maxInactiveIntervalInSeconds = 1800,
    redisNamespace = "birman-cat:session",
)
class SessionConfiguration {

    @Bean
    fun springSessionDefaultRedisSerializer(): RedisSerializer<Any> {
        return GenericJackson2JsonRedisSerializer(Jackson.getMapper())
    }

    @Bean
    fun sessionRedisOperations(
        redisConnectionFactory: RedisConnectionFactory,
    ): RedisOperations<String, Any> {
        return RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory
            hashKeySerializer = StringRedisSerializer()
            keySerializer = StringRedisSerializer()
        }
    }
}
