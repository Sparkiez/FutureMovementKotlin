package com.waleong.futuremovement.util

import android.content.Context

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader


/**
 * A generic file reader.
 * Created by raymondleong on 03,July,2019
 */
object FileParserUtil {

    interface OnLineParsedListener {
        fun onLineParsed(line: String)
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun readFile(context: Context, rawResId: Int, listener: OnLineParsedListener? = null): String {

        val inputStream = context.resources.openRawResource(rawResId) // getting XML
        val inputStreamReader = InputStreamReader(inputStream)
        val buffreader = BufferedReader(inputStreamReader as Reader?)
        //Read text from file
        val text = StringBuilder()

        var line = buffreader.readLine()

        while (line != null) {
            text.append(line)
            text.append('\n')


            listener?.onLineParsed(line)
            line = buffreader.readLine()
        }

        return text.toString()
    }
}
