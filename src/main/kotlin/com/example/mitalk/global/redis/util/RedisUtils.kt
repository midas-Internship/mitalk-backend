package com.example.mitalk.global.redis.util

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class RedisUtils(
        private val redisTemplate: RedisTemplate<String, Any>
) {

    /**
     * @desc: Sorted Set 삭제.
     */
    fun delete(key: String): Boolean {
        return redisTemplate.delete(key)
    }

    /**
     * @desc: Sorted Set 조회
     */
    fun getValue(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }

    /**
     * @desc: RedisTemplate에 SortedSet 초기화.
     */
    fun opsForZSet(): ZSetOperations<String, Any> {
        return redisTemplate.opsForZSet()
    }

    /**
     * @desc: Sorted Set 자료형 사이즈
     */
    fun zCard(str: String): Long? {
        val z: ZSetOperations<String, Any> = redisTemplate.opsForZSet()
        return z.size(str)
    }

    /**
     * @desc: Sorted Set 자료형 start ~ end 까지 조회.
     */
    fun zRange(key: String, start: Long, end: Long): MutableSet<Any>? {
        return opsForZSet().range(key, start, end)
    }

    /**
     * @desc: Sorted Set 자료형 Value의 현재위치 조회.
     */
    fun getzRank(key: String, value: String): Long? {
        return opsForZSet().rank(key, value!!)
    }

    fun createObject(key: String, value: Any) = redisTemplate.opsForZSet().add(key, value, System.currentTimeMillis().toDouble())

    fun createTimeOutObject(key: String, value: String, timeout: Duration) = redisTemplate.opsForValue().set(key, value , timeout)
}