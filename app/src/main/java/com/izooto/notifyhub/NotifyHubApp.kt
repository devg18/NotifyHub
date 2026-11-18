package com.izooto.notifyhub

import android.app.Application
import com.izooto.notifyhub.notification.NotificationHelper

class NotifyHubApp : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this)
    }
}
