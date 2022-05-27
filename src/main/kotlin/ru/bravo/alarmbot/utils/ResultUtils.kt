package ru.bravo.alarmbot.utils

fun <T> Exception.toResult() =
    Result.failure<T>(Throwable(this))

fun <T> Throwable.toResult() =
    Result.failure<T>(this)
