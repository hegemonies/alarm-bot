package ru.bravo.alarmbot.utils

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.quartz.CronExpression
//import ru.bravo.alarmbot.service.AlarmDateFormat
import java.util.Date

fun getAlarmDateFormatFromString(dateTime: String): Result<AlarmDateFormat> {
    val regexpToHandler = listOf(
        "\\d{1,2}:\\d{1,2}".toRegex()
                to DateTimeHandler::hoursMinutes, // 10:30

        "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}".toRegex()
                to DateTimeHandler::dayMonthYearHoursMinutes, // 10-08-2021 10:30

        "\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}".toRegex()
                to DateTimeHandler::dayMonthHoursMinutes, // 10-08 10:30
    ).toMap()

    regexpToHandler.forEach { (regexp, handler) ->
        if (regexp.matches(dateTime)) {
            return Result.success(
                SimpleDate(
                    date = handler(dateTime)
                )
            )
        }
    }

    if (CronExpression.isValidExpression(dateTime)) {
        return Result.success(
            CronData(
                cronExpression = CronExpression(dateTime)
            )
        )
    }

    return Result.failure(
        Exception("Can not find regexp [${regexpToHandler.keys} + cronExpression] for enter date [$dateTime]")
    )
}

private object DateTimeHandler {

    /**
     * Parse [time] be like *10:30* to current date [Date] with [time].
     * Example: if [time] is *10:30* and current date is *2021-09-22*, then [Date] will be *2021-09-22 10:30*.
     */
    fun hoursMinutes(time: String): Date =
        LocalDateTime.now().let { dateTimeNow ->
            LocalDateTime.parse(time, DateTimeFormat.forPattern("HH:mm"))
                .withYear(dateTimeNow.year)
                .withMonthOfYear(dateTimeNow.monthOfYear)
                .withDayOfMonth(dateTimeNow.dayOfMonth)
                .toDate()
        }

    /**
     * Parse [dateTime] be like *10-08-2021 10:30* to current date [Date] with [dateTime].
     */
    fun dayMonthYearHoursMinutes(dateTime: String): Date =
        LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("dd-MM-yyyy HH:mm"))
            .toDate()

    /**
     * Parse [dateTime] be like *10-08 10:30* to date [Date] with current year.
     * Example: if [dateTime] is *10-08 10:30* and current year is *2021*, then [Date] will be *2021-10-08 10:30*.
     */
    fun dayMonthHoursMinutes(dateTime: String): Date =
        LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("dd-MM HH:mm"))
            .withYear(LocalDateTime.now().year)
            .toDate()
}
