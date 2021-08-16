package ru.bravo.alarmbot.repository

import org.springframework.data.redis.core.RedisTemplate

class TaskRepository : RedisTemplate<Long, String>()
