package ru.bravo.alarmbot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "telegram-bot")
data class TelegramBotProperties(

    val token: String
)
