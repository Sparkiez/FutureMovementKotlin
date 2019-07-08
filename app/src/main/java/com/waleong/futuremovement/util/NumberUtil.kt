package com.waleong.futuremovement.util

/**
 * A util to parse string into an Integer object.
 * Created by raymondleong on 03,July,2019
 */
object NumberUtil {
    fun parseInteger(numberText: String?): Int? {
        try {
            return numberText?.toInt()
        } catch (e: Exception) {
        }

        return null
    }
}
