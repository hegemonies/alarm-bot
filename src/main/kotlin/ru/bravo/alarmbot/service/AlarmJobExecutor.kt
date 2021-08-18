package ru.bravo.alarmbot.service

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class AlarmJobExecutor(

) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {

    }
}
