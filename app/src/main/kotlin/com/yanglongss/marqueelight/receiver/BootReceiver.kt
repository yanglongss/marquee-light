package com.yanglongss.marqueelight.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.yanglongss.marqueelight.data.PreferenceManager

class NotificationListenerService : NotificationListenerService() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!preferenceManager.isEnabled) return

        val packageName = sbn.packageName
        val notificationId = sbn.id
        val notification = sbn.notification

        val type = when {
            packageName.contains("sms") || packageName.contains("messaging") -> 1
            packageName.contains("dialer") || packageName.contains("phone") -> 2
            else -> 3
        }

        Log.d(TAG, "Notification posted from $packageName (type=$type, id=$notificationId)")

        val intent = Intent(this, MarqueeLightService::class.java).apply {
            action = MarqueeLightService.ACTION_TRIGGER_PULSE
            putExtra(MarqueeLightService.EXTRA_NOTIFICATION_TYPE, type)
        }
        startService(intent)
    }

    companion object {
        private const val TAG = "NotificationListener"
    }
}
