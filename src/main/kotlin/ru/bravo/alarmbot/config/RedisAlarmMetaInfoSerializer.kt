package ru.bravo.alarmbot.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.data.redis.serializer.RedisSerializer
import ru.bravo.alarmbot.model.AlarmMetaInfo

class RedisAlarmMetaInfoSerializer : RedisSerializer<AlarmMetaInfo> {

    override fun serialize(value: AlarmMetaInfo?): ByteArray {
        value ?: throw RuntimeException("Value must be not null")
        return Json.encodeToString(value).toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): AlarmMetaInfo {
        bytes ?: throw RuntimeException("Bytes must by not null")
        return Json.decodeFromString(bytes.decodeToString())
    }
}
