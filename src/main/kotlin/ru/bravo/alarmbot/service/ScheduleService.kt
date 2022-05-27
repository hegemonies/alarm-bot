package ru.bravo.alarmbot.service

import org.quartz.CronExpression
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobKey
import org.quartz.TriggerBuilder
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import ru.bravo.alarmbot.constant.JobDataMapFieldName
import ru.bravo.alarmbot.scheduler.job.AlarmJob
import ru.bravo.alarmbot.utils.toResult
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone
import java.util.UUID

@Service
class ScheduleService(
    private val schedulerFactory: SchedulerFactoryBean
) {

    /**
     * @return pair: first is *alarm-id*, second is *next-fire-date-time*
     */
    fun scheduleAlarmJob(
        chatId: Long,
        alarmName: String,
        description: String,
        date: Date
    ): Result<Pair<String, Date>> {
        val identity = alarmName + UUID.randomUUID().toString()

        val nextFireDate = runCatching {
            val job = JobBuilder.newJob(AlarmJob::class.java)
                .setJobData(
                    createJobData(chatId, description)
                )
                .storeDurably()
                .withIdentity(identity)
                .withDescription("Alarm")
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .withIdentity(identity)
                .startAt(date)
                .build()

            schedulerFactory.scheduler.scheduleJob(job, trigger)
        }.getOrElse { error ->
            return error.toResult()
        }

        return Result.success(
            identity to nextFireDate
        )
    }

    /**
     * @return pair: first is *alarm-id*, second is *next-fire-date-time*
     */
    fun scheduleAlarmJob(
        chatId: Long,
        alarmName: String,
        description: String,
        cronExpression: CronExpression
    ): Result<Pair<String, Date>> {
        val identity = alarmName + UUID.randomUUID().toString()

        val nextFireDate = runCatching {
            val job = JobBuilder.newJob(AlarmJob::class.java)
                .setJobData(
                    createJobData(chatId, description)
                )
                .storeDurably()
                .withIdentity(identity)
                .withDescription("Alarm")
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .withIdentity(identity)
                .withSchedule(
                    CronScheduleBuilder.cronSchedule(cronExpression)
                        .inTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                )
                .build()

            schedulerFactory.scheduler.scheduleJob(job, trigger)
        }.getOrElse { error ->
            return error.toResult()
        }

        return Result.success(
            identity to nextFireDate
        )
    }

    private fun createJobData(chatId: Long, description: String): JobDataMap =
        JobDataMap(
            mapOf(
                JobDataMapFieldName.CHAT_ID to chatId,
                JobDataMapFieldName.DESCRIPTION to description
            )
        )

    fun deleteAlarmJob(alarmName: String): Result<Unit> = runCatching {
        schedulerFactory.scheduler.deleteJob(JobKey(alarmName))
    }
}
