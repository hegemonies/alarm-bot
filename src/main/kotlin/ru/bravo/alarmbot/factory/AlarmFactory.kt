package ru.bravo.alarmbot.factory

import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.TriggerBuilder
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import ru.bravo.alarmbot.service.AlarmJobExecutor
import java.util.Date
import java.util.UUID

@Service
class AlarmFactory(
    private val schedulerFactory: SchedulerFactoryBean
) {

    fun createAlarm(alarmName: String, date: Date) {
        val identity = alarmName + UUID.randomUUID().toString()

        val job = JobBuilder.newJob(AlarmJobExecutor::class.java)
            .setJobData(
                JobDataMap()
            )
            .storeDurably()
            .withIdentity(identity)
            .withDescription("Clear device inventory cache")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .forJob(job)
            .withIdentity(identity)
            .startAt(date)
            .build()

        schedulerFactory.scheduler.scheduleJob(job, trigger)
    }
}
