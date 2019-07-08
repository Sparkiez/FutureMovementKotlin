package com.waleong.futuremovement.util

import android.content.Context
import android.os.Environment

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

import timber.log.Timber

/**
 * A generic file writer.
 * Created by raymondleong on 03,July,2019
 */
object FileWriterUtil {

    /**
     * @param context
     * @param data
     * @param fileName
     * @return path to the files that was written in if successful.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeToFile(context: Context, data: String, fileName: String): String {
        val path = context.getExternalFilesDir(null)
        val outputFile = File(path, fileName)
        Timber.tag(FileWriterUtil::class.java.toString()).v("writeToFile() : attempting to write to path " + outputFile.path)

        val outputStreamWriter = FileOutputStream(outputFile)
        outputStreamWriter.write(data.toByteArray())
        outputStreamWriter.close()

        return outputFile.path
    }
}
