package ru.bravo.alarmbot.config

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object AlarmBotCoroutineScope : CoroutineScope {

    override val coroutineContext: CoroutineContext = EmptyCoroutineContext
}
