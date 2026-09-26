package com.yanglongss.marqueelight.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yanglongss.marqueelight.data.PreferenceManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val preferences = PreferenceManager(context)
            if (preferences.isEnabled) {
                val serviceIntent = Intent(context, com.yanglongss.marqueelight.service.MarqueeLightService::class.java).apply {
                    action = com.yanglongss.marqueelight.service.MarqueeLightService.ACTION_START
                }
                context.startService(serviceIntent)
                Log.d(TAG, "Boot completed; marquee service started")
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}
