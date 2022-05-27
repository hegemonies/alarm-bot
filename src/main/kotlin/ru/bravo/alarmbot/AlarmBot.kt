package ru.bravo.alarmbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.network.fold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.bravo.alarmbot.config.properties.TelegramBotProperties
import ru.bravo.alarmbot.constant.MessageConstant
import ru.bravo.alarmbot.service.AlarmBotService
import ru.bravo.alarmbot.utils.getChatId
import ru.bravo.alarmbot.utils.getChatName
import ru.bravo.alarmbot.utils.getMessageId
import ru.bravo.alarmbot.utils.getUserId
import ru.bravo.alarmbot.utils.getUsername

@Component
class AlarmBot(
    private val telegramBotProperties: TelegramBotProperties,
    @Qualifier("alarmBotCoroutineScope") private val alarmBotCoroutineScope: CoroutineScope,
    private val alarmBotService: AlarmBotService
) {

    @EventListener(ApplicationReadyEvent::class)
    fun execute() {
        alarmBotCoroutineScope.launch {
            runBot()
        }
    }

    private suspend fun runBot() {
        logger.info("Start bot")

        val bot = bot {
            token = telegramBotProperties.token

            dispatch {
                command("start") {
                    bot.sendMessage(
                        chatId = getChatId(),
                        text = MessageConstant.HELP_MESSAGE,
                        replyToMessageId = getMessageId()
                    )
                }

                /**
                 * message format: date | description
                 *
                 * create quartz job
                 * save metainfo to database
                 * metainfo: user_id, alarm_id, date_string, created_at
                 *
                 */
                command("createAlarm") {
                    val input = this.message.text
                        ?: run {
                            bot.sendMessage(
                                chatId = getChatId(),
                                text = "Message must be not empty",
                                replyToMessageId = getMessageId()
                            )
                            return@command
                        }

                    val (alarmId, nextFireDate) = alarmBotService.createAlarm(
                        userId = getUserId(),
                        chatId = getChatId().id,
                        input = input.substringAfter(" ")
                    ).getOrElse { error ->
                        bot.sendMessage(
                            chatId = getChatId(),
                            text = "Can not create alarm: ${error.message}",
                            replyToMessageId = getMessageId()
                        )
                        return@command
                    }

                    logger.debug(
                        "The alarm was created successfully\n" +
                                "Next fire date: $nextFireDate\n" +
                                "AlarmId: $alarmId\n" +
                                "From user: ${getUsername()}, ${getUserId()}\n" +
                                "In chat: ${getChatId()}, ${getChatName()}"
                    )

                    bot.sendMessage(
                        chatId = getChatId(),
                        text = "The alarm was created successfully\n" +
                                "Next fire date: $nextFireDate\n" +
                                "AlarmId: $alarmId",
                        replyToMessageId = getMessageId()
                    )
                }

                command("deleteAlarm") {
                    val userId = this.message.from?.id ?: return@command
                    val alarmId = this.args.first()

                    logger.info("alarm id = $alarmId")

                    logger.debug("User [${getUsername()}, $userId] delete alarm [$alarmId]")

                    alarmBotService.deleteAlarm(
                        alarmId = alarmId,
                        userId = userId
                    ).onFailure { error ->
                        bot.sendMessage(
                            chatId = getChatId(),
                            text = "Can not delete the alarm [$alarmId]: ${error.message}",
                            replyToMessageId = getMessageId()
                        )

                        return@command
                    }

                    bot.sendMessage(
                        chatId = getChatId(),
                        text = "The alarm was deleted successfully\n" +
                                "AlarmId: $alarmId",
                        replyToMessageId = getMessageId()
                    )
                }

                command("updateAlarmDescription") {
                    bot.sendMessage(
                        chatId = getChatId(),
                        text = MessageConstant.IN_WORK,
                        replyToMessageId = getMessageId()
                    )
                }

                command("help") {
                    bot.sendMessage(
                        chatId = getChatId(),
                        text = MessageConstant.HELP_MESSAGE,
                        replyToMessageId = getMessageId()
                    )
                }

                command("status") {}

                command("getAlarms") {}
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

    //    @EventListener(ApplicationReadyEvent::class)
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
