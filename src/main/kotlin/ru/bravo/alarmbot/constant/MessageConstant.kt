package ru.bravo.alarmbot.constant

object MessageConstant {

    const val HELP_MESSAGE = """
Hi from Bravo team!

Commands:
/createAlarm date-time | description            : creating alarm clock on date-time with description
/deleteAlarm alarm-id                           : delete alarm clock if you are the creator of it
/updateAlarmDescription alarm-id | description  : update alarm description if you are the creator of it
/help                                           : help
/status : print status and version of alarm-bot
/getAlarms : print your own alarm

Useful:
*date-time* can be:
- format = 'hours:minutes', example = '10:30', fire time if today is 2021-11-11 = '2021-11-11 10:30'
- format = 'year-month-day_of_month hours:minutes', example = '2011-11-11 10:30', fire time = '2021-11-11 10:30'
- format = 'month-day_of_month hours:minutes', example = '11-11 10:30', fire time if current year is 2021 = '2021-11-11 10:30'
- format = 'quartz-cron', example = '0 30 10 ? * 2-6', fire time if today is sunday 2021-08-29 = '2021-08-30 10:30', https://www.freeformatter.com/cron-expression-generator-quartz.html
    """

    const val IN_WORK = "Sorry. The feature in progress."
}
