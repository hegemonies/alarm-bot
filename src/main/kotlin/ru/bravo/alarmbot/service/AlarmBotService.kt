package ru.bravo.alarmbot.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.bravo.alarmbot.model.AlarmMetaInfo
import ru.bravo.alarmbot.repository.AlarmMetaInfoRepository
import ru.bravo.alarmbot.utils.AlarmDateFormat
import ru.bravo.alarmbot.utils.CronData
import ru.bravo.alarmbot.utils.SimpleDate
import ru.bravo.alarmbot.utils.getAlarmDateFormatFromString
import ru.bravo.alarmbot.utils.toResult
import java.time.Clock
import java.util.Date

@Service
class AlarmBotService(
    private val scheduleService: ScheduleService,
    private val alarmMetaInfoRepository: AlarmMetaInfoRepository
) {

    /**
     * Steps:
     * * parse input
     * * schedule quartz job
     */
    fun createAlarm(userId: Long, chatId: Long, input: kotlin.String): Result<Pair<kotlin.String, Date>> {
        val (description, date) = parseInput(input).getOrElse { error ->
            logger.error("Can not parse input string", error)

            return error.toResult()
        }

        val alarmName = "$userId-$chatId-"

        val result = when (date) {
            is SimpleDate -> scheduleService.scheduleAlarmJob(
                chatId = chatId,
                alarmName = alarmName,
                description = description,
                date = date.date
            )

            is CronData -> scheduleService.scheduleAlarmJob(
                chatId = chatId,
                alarmName = alarmName,
                description = description,
                cronExpression = date.cronExpression
            )
        }

        if (result.isSuccess) {
            result.getOrNull()?.also { (alarmId, _) ->
                runCatching {
                    alarmMetaInfoRepository.opsForList().leftPush(
                        alarmId,
                        AlarmMetaInfo(
                            alarmId = alarmId,
                            userId = userId,
                            description = description,
                            dateString = date.toString(),
                            createdAt = Clock.systemUTC().millis()
                        )
                    )
                }.onFailure { error ->
                    logger.error("Can not save alarm meta info: ${error.message}")
                }
            }
        }

        return result
    }

    /**
     * @param input raw input string.
     *
     * @return pair where first is a *description*, and second is *simple date* or *cron expression*.
     */
    private fun parseInput(input: kotlin.String): Result<Pair<kotlin.String, AlarmDateFormat>> {
        val (dateTime, description) = input.split("|")
        val alarmDateFormat = getAlarmDateFormatFromString(dateTime.trim()).getOrElse { error ->
            return error.toResult()
        }

        return Result.success(
            description to alarmDateFormat
        )
    }

    fun deleteAlarm(alarmId: kotlin.String, userId: Long): Result<Unit> {
        val alarmMetaInfo = alarmMetaInfoRepository.opsForList().index(alarmId, 0).also {
            logger.debug("alarm meta info = $it")
        }

        return if (alarmMetaInfo?.userId == userId) {
            scheduleService.deleteAlarmJob(alarmName = alarmId)
        } else {
            Result.failure(
                Exception("You can not delete an alarm clock that is not your own.")
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
