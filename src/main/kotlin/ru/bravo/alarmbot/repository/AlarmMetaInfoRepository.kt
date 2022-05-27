package ru.bravo.alarmbot.repository

import org.springframework.data.redis.core.RedisTemplate
import ru.bravo.alarmbot.model.AlarmMetaInfo

class AlarmMetaInfoRepository : RedisTemplate<kotlin.String, AlarmMetaInfo>()
