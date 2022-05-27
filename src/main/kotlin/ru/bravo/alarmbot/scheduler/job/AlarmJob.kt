package ru.bravo.alarmbot.scheduler.job

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import ru.bravo.alarmbot.config.properties.TelegramBotProperties
import ru.bravo.alarmbot.constant.JobDataMapFieldName

@Component
class AlarmJob(
    private val telegramBotProperties: TelegramBotProperties
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        runCatching {
            val chatId = context.mergedJobDataMap[JobDataMapFieldName.CHAT_ID] as Long
            val description = context.mergedJobDataMap[JobDataMapFieldName.DESCRIPTION] as String

            val bot = bot { token = telegramBotProperties.token }

            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = description + if (context.nextFireTime != null) {
                    "\nNext fire time: ${context.nextFireTime}"
                } else {
                    ""
                }
            )
        }.onFailure { error ->
            logger.error("Can not execute alarm job: ${error.message}")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
