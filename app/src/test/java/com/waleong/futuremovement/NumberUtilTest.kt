package com.waleong.futuremovement

import com.waleong.futuremovement.util.NumberUtil

import org.junit.Test
import org.junit.Assert.*

/**
 * Created by raymondleong on 03,July,2019
 */
class NumberUtilTest {
    @Test
    fun testInteger() {
        val number = NumberUtil.parseInteger("1")
        assert(number == 1)
    }

    @Test
    fun testNegativeInteger() {
        val number = NumberUtil.parseInteger("-1")
        assert(number == -1)
    }

    @Test
    fun testInvalidInteger() {
        val number = NumberUtil.parseInteger("askjnsd")
        assertNull(number)
    }

    @Test
    fun testEmptyInteger() {
        val number = NumberUtil.parseInteger("")
        assertNull(number)
    }

    @Test
    fun testIsNull() {
        val something : String? = null

        if (something == null) {
            println("Something is ${something?.toInt()}")
        }

        assert(true);
    }
}
