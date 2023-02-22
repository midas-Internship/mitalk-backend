package com.example.mitalk.domain.customer.domain.entity

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class CustomerQueue(
        private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        const val KEY = "CustomerQueue"
        const val MAX_SIZE = 1
    }

//    /**
//     * @desc: Sorted Set 삭제.
//     */
//    fun delete(key: String): Boolean {
//        return redisTemplate.delete(key)
//    }
//
//    /**
//     * @desc: Sorted Set 조회
//     */
//    fun getValue(key: String): Any? {
//        return redisTemplate.opsForValue().get(key)
//    }

    /**
     * @desc: RedisTemplate에 SortedSet 초기화.
     */
    fun opsForZSet() = redisTemplate.opsForZSet()

    /**
     * @desc: Sorted Set 자료형 사이즈
     */
    fun zSize() = opsForZSet().size(KEY) ?: 0

    // 꽉참 = true, 비어있음 = false
    fun isQueueFull() = zSize() > MAX_SIZE

    /**
     * @desc: Sorted Set 자료형 start ~ end 까지 조회.
     */
    fun zRange(start: Long, end: Long) = (opsForZSet().range(KEY, start, end) ?: emptySet()).toList()

    fun zRangeAndDelete(start: Long, end: Long): List<String> {
        val range = opsForZSet().range(KEY, start, end) ?: emptySet()
        opsForZSet().removeRange(KEY, start, end)
        return range.toList()
    }
    /**
     * @desc: Sorted Set 자료형 Value의 현재위치 조회.
     */

    fun createTimeOutObject(key: String, value: String, timeout: Duration) = redisTemplate.opsForValue().set(key, value , timeout)

    fun zRank(value: String) = opsForZSet().rank(KEY, value) ?: 0

    fun zAdd(value: String) = opsForZSet().add(KEY, value, System.currentTimeMillis().toDouble())

    fun zDelete(value: String) = opsForZSet().remove(KEY, value) ?: 0
}