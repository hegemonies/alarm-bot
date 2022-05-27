package ru.bravo.alarmbot.utils

import org.quartz.CronExpression
import java.util.Date

sealed class AlarmDateFormat {
    override fun toString(): String {
        return when (this) {
            is SimpleDate -> this.getString()
            is CronData -> this.getString()
        }
    }
}

data class SimpleDate(val date: Date) : AlarmDateFormat() {
    fun getString(): String {
        return date.toString()
    }
}

data class CronData(val cronExpression: CronExpression) : AlarmDateFormat() {
    fun getString(): String {
        return cronExpression.toString()
    }
}
