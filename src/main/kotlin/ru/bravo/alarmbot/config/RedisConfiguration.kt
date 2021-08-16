package ru.bravo.alarmbot.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import ru.bravo.alarmbot.repository.TaskRepository

@Configuration
class RedisConfiguration {

    @Bean
    fun redisConnectionFactory(redisProperties: RedisProperties) =
        LettuceConnectionFactory(RedisStandaloneConfiguration(redisProperties.host, redisProperties.port))

    @Bean
    fun taskRepository(connectionFactory: LettuceConnectionFactory) =
        TaskRepository().also {
            it.setConnectionFactory(connectionFactory)
        }
}
