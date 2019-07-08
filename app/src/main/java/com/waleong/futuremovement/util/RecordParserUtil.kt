package com.waleong.futuremovement.util

import android.text.TextUtils

import com.waleong.futuremovement.model.ColumnDefinition

import java.util.ArrayList
import java.util.HashMap

/**
 * A parser to parse a transactional record into a key pair, based on the column key, and its value.
 * Created by raymondleong on 03,July,2019
 */
object RecordParserUtil {

    /**
     * @param columnDefinitions
     * @param line
     * @return a mapping of the column values to its respective column key.
     */
    fun parse(columnDefinitions: List<ColumnDefinition>, line: String): Map<String, String> {
        val keyPair = HashMap<String, String>()

        if (TextUtils.isEmpty(line)) {
            return keyPair
        }

        for (columnDefinition in columnDefinitions) {
            val key = columnDefinition.name
            val colStart = columnDefinition.columnStart - 1
            val colEnd = columnDefinition.columnEnd

            if (colStart < 0 || colEnd > line.length) {
                continue
            }

            val value = line.substring(colStart, colEnd)
            keyPair[key] = value
        }

        return keyPair
    }
}
