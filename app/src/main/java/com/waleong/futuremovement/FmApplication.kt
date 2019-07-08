package com.waleong.futuremovement

import android.app.Application

import timber.log.Timber

/**
 * Created by raymondleong on 03,July,2019
 */
class FmApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
