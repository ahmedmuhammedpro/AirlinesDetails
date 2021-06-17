package com.ahmed.airlinesdetails

import android.app.Application
import timber.log.Timber
import java.math.BigDecimal

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}