package com.alekseykostyunin.enot

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
    }

}