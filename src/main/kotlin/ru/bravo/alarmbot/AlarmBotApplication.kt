package ru.bravo.alarmbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AlarmBotApplication

fun main(args: Array<String>) {
	runApplication<AlarmBotApplication>(*args)
}
