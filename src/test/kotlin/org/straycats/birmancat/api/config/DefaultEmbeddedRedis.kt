package org.straycats.birmancat.api.config

import com.asarkar.spring.test.redis.EmbeddedRedisLifecycle
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.annotation.Order
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.straycats.birmancat.api.common.Constant

@Lazy(false)
@TestConfiguration
class DefaultEmbeddedRedis(
    private val properties: RedisProperties,
) {
    @Value("\${embedded-redis.port:-1}")
    var port: Int = 0

    @Bean
    fun redisClusterConfiguration(): RedisClusterConfiguration {
        return RedisClusterConfiguration(
            listOf("${properties.host}:$port"),
        )
    }

    @Bean(name = [Constant.Redis.USER_LOCK])
    fun userLockRedisTemplate(): StringRedisTemplate {
        return mockTemplate()
    }

    private fun mockTemplate(): StringRedisTemplate {
        val factory = redisConnectionFactory()

        return StringRedisTemplate().apply {
            connectionFactory = factory
        }
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val configuration = RedisStandaloneConfiguration(properties.host, port)
        return LettuceConnectionFactory(configuration).apply {
            afterPropertiesSet()
        }
    }
}

/**
 * Spring Lifecycle에서 Bean 리스트에 대한 start()를 할 때 Order 설정이 없으면
 * RedisIndexedHttpSessionConfiguration bean이 먼저 실행되어 Redis에 Connection을 하고 오류를 내는 상황이 발생함.
 * 따라서 Order를 줘서 먼저 실행되도록 변경한다
 *
 * @author fennec-fox
 */
@Order(1)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(
    name = [
        "redis.embedded.RedisServer",
    ],
)
class CustomEmbeddedRedisAutoConfiguration {
    @Bean
    fun embeddedRedisLifecycle(): EmbeddedRedisLifecycle {
        return EmbeddedRedisLifecycle()
    }
}
