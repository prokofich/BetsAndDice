package com.example.betsdice.application

import android.app.Application
import com.onesignal.OneSignal

class OneSignalApplication:Application() {

    val ONESIGNALL_APP_ID = "d5f73a51-0bc1-4cda-ab93-efe9550205f5"

    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNALL_APP_ID)
    }

}