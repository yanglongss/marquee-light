package com.yanglongss.marqueelight.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isEnabled: Boolean
        get() = preferences.getBoolean(KEY_ENABLED, false)
        set(value) = preferences.edit { putBoolean(KEY_ENABLED, value) }

    var defaultColor: Int
        get() = preferences.getInt(KEY_DEFAULT_COLOR, DEFAULT_COLOR)
        set(value) = preferences.edit { putInt(KEY_DEFAULT_COLOR, value) }

    var speed: Float
        get() = preferences.getFloat(KEY_SPEED, DEFAULT_SPEED)
        set(value) = preferences.edit { putFloat(KEY_SPEED, value) }

    var smsColor: Int
        get() = preferences.getInt(KEY_SMS_COLOR, SMS_COLOR)
        set(value) = preferences.edit { putInt(KEY_SMS_COLOR, value) }

    var callColor: Int
        get() = preferences.getInt(KEY_CALL_COLOR, CALL_COLOR)
        set(value) = preferences.edit { putInt(KEY_CALL_COLOR, value) }

    var appNotificationColor: Int
        get() = preferences.getInt(KEY_APP_NOTIFICATION_COLOR, APP_NOTIFICATION_COLOR)
        set(value) = preferences.edit { putInt(KEY_APP_NOTIFICATION_COLOR, value) }

    var powerSavingMode: Boolean
        get() = preferences.getBoolean(KEY_POWER_SAVING_MODE, false)
        set(value) = preferences.edit { putBoolean(KEY_POWER_SAVING_MODE, value) }

    companion object {
        private const val PREFS_NAME = "marquee_light_prefs"
        private const val KEY_ENABLED = "is_enabled"
        private const val KEY_DEFAULT_COLOR = "default_color"
        private const val KEY_SPEED = "speed"
        private const val KEY_SMS_COLOR = "sms_color"
        private const val KEY_CALL_COLOR = "call_color"
        private const val KEY_APP_NOTIFICATION_COLOR = "app_notification_color"
        private const val KEY_POWER_SAVING_MODE = "power_saving_mode"

        // Default colors (ARGB)
        private const val DEFAULT_COLOR = 0xFF00FF00.toInt()  // Green
        private const val SMS_COLOR = 0xFFFF6B00.toInt()      // Orange
        private const val CALL_COLOR = 0xFFFF0000.toInt()     // Red
        private const val APP_NOTIFICATION_COLOR = 0xFF0099FF.toInt()  // Blue
        private const val DEFAULT_SPEED = 1.0f
    }
}
