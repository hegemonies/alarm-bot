package ru.bravo.alarmbot.utils

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId

fun CommandHandlerEnvironment.getChatId() =
    ChatId.fromId(this.message.chat.id)

fun CommandHandlerEnvironment.getChatName() =
    this.message.chat.username ?: ""

fun CommandHandlerEnvironment.getUserId() =
    this.message.from?.id ?: 0L

fun CommandHandlerEnvironment.getUsername() =
    this.message.from?.username ?: ""

fun CommandHandlerEnvironment.getMessageId() =
    this.message.messageId
