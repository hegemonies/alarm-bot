package ru.bravo.alarmbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.contact
import com.github.kotlintelegrambot.dispatcher.handlers.ErrorHandler
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.network.fold
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.bravo.alarmbot.config.AlarmBotCoroutineScope
import ru.bravo.alarmbot.config.properties.TelegramBotProperties

@Component
class AlarmBot(
    private val telegramBotProperties: TelegramBotProperties
) {

    @EventListener(ApplicationReadyEvent::class)
    fun execute() {
        AlarmBotCoroutineScope.launch {
            runBot()
        }
    }

    private suspend fun runBot() {
        logger.info("Start bot")

        val bot = bot {
            token = telegramBotProperties.token

            dispatch {
                command("start") {
                    logger.debug("user id = ${this.message.from?.id}")
                    logger.debug("chat id = ${message.chat.id}")

                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Hi!").fold(
                        { response ->
                            logger.debug("response on command 'start' is $response")
                        },
                        { error ->
                            logger.error("Can not send message on command 'start': ${error.exception?.message}")
                        }
                    )
                }

                // message format: "date" "description"
                // regexp: "%t"%s"%t"
                command("createAlarm") {
                    this.message.text
                }

                command("deleteAlarm") {

                }

                command("updateAlarmDescription") {

                }
            }
        }

        bot.startPolling()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}

@Component
class Foo(
    private val telegramBotProperties: TelegramBotProperties
) {

    @EventListener(ApplicationReadyEvent::class)
    fun test() {
        bot {
            token = telegramBotProperties.token
        }.also { bot ->
            bot.sendMessage(
                chatId = ChatId.fromId(94977905),
                text = "ping"
            )
        }
    }
}
