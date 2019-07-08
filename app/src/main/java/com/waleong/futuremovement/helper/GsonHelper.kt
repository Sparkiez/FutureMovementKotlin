package com.waleong.futuremovement.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.lang.reflect.Type

import timber.log.Timber

/**
 * A helper to help us serialise/deserialise gson data.
 * Created by raymondleong on 03,July,2019
 */
object GsonHelper {

    private var mGson: Gson? = null
    private val TAG = "GsonHelper"

    private val instance: Gson
        get() {
            if (mGson == null) {
                mGson = GsonBuilder().setLenient().enableComplexMapKeySerialization().serializeNulls().create()
            }

            return mGson!!
        }

    fun <T> parse(json: String, typeOfT: Type): T? {
        try {
            return GsonHelper.instance.fromJson<T>(json, typeOfT)
        } catch (e: Exception) {
            Timber.tag(TAG).v("parse() : with exception as " + e.message)
        }

        return null
    }
}

