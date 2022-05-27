package ru.bravo.alarmbot.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.data.redis.serializer.RedisSerializer

class RedisStringSerializer : RedisSerializer<String> {

    override fun serialize(value: String?): ByteArray {
        value ?: throw RuntimeException("Value must be not null")
        return Json.encodeToString(value).toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): String {
        bytes ?: throw RuntimeException("Bytes must by not null")
        return Json.decodeFromString(bytes.decodeToString())
    }
}
