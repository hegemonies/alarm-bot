package ru.bravo.alarmbot.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@Configuration
class CoroutineScopeConfiguration {

    @Bean(name = ["alarmBotCoroutineScope"])
    fun alarmBotCoroutineScope() =
        object : CoroutineScope {
            override val coroutineContext: CoroutineContext =
                Executors.newFixedThreadPool(2).asCoroutineDispatcher()
        }
}
