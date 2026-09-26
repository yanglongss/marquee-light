package com.yanglongss.marqueelight.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.yanglongss.marqueelight.data.PreferenceManager
import com.yanglongss.marqueelight.ui.view.MarqueeView

class MarqueeLightService : Service() {

    private lateinit var preferenceManager: PreferenceManager
    private var marqueeView: MarqueeView? = null

    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
        Log.d(TAG, "MarqueeLightService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MarqueeLightService started")

        when (intent?.action) {
            ACTION_START -> {
                startMarqueeAnimation()
            }
            ACTION_STOP -> {
                stopMarqueeAnimation()
            }
            ACTION_TRIGGER_PULSE -> {
                val notificationType = intent.getIntExtra(EXTRA_NOTIFICATION_TYPE, 0)
                triggerPulse(notificationType)
            }
        }

        return START_STICKY
    }

    private fun startMarqueeAnimation() {
        if (preferenceManager.isEnabled && marqueeView != null) {
            marqueeView?.setColor(preferenceManager.defaultColor)
            marqueeView?.setSpeed(preferenceManager.speed)
            marqueeView?.startAnimation()
            Log.d(TAG, "Marquee animation started")
        }
    }

    private fun stopMarqueeAnimation() {
        marqueeView?.stopAnimation()
        Log.d(TAG, "Marquee animation stopped")
    }

    private fun triggerPulse(notificationType: Int) {
        val color = when (notificationType) {
            NOTIFICATION_TYPE_SMS -> preferenceManager.smsColor
            NOTIFICATION_TYPE_CALL -> preferenceManager.callColor
            NOTIFICATION_TYPE_APP -> preferenceManager.appNotificationColor
            else -> preferenceManager.defaultColor
        }

        marqueeView?.setColor(color)
        marqueeView?.triggerPulse(3000)  // 3 seconds as specified
        Log.d(TAG, "Marquee pulse triggered for notification type: $notificationType")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMarqueeAnimation()
        Log.d(TAG, "MarqueeLightService destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "MarqueeLightService"
        const val ACTION_START = "com.yanglongss.marqueelight.action.START"
        const val ACTION_STOP = "com.yanglongss.marqueelight.action.STOP"
        const val ACTION_TRIGGER_PULSE = "com.yanglongss.marqueelight.action.TRIGGER_PULSE"
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"

        const val NOTIFICATION_TYPE_SMS = 1
        const val NOTIFICATION_TYPE_CALL = 2
        const val NOTIFICATION_TYPE_APP = 3
    }
}
