package com.yanglongss.marqueelight

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MarqueeLightApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Marquee Light Status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Marquee Light background service notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "marquee_light_channel"
    }
}
