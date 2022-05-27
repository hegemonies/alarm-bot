package ru.bravo.alarmbot.model

import kotlinx.serialization.Serializable

@Serializable
data class AlarmMetaInfo(
    val alarmId: String, // userId-charId-uuid
    val userId: Long,
    val description: String,
    val dateString: String,
    val createdAt: Long
)
