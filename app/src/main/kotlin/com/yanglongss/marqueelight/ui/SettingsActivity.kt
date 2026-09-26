package com.yanglongss.marqueelight.ui

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.yanglongss.marqueelight.data.PreferenceManager
import com.yanglongss.marqueelight.databinding.ActivityMainBinding
import com.yanglongss.marqueelight.service.MarqueeLightService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        updateUI()
        setupListeners()
    }

    private fun setupListeners() {
        binding.toggleButton.setOnClickListener {
            val enabled = !preferenceManager.isEnabled
            preferenceManager.isEnabled = enabled
            updateUI()
            if (enabled) {
                startService()
            } else {
                stopService()
            }
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.enableNotificationAccessButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    private fun updateUI() {
        binding.statusText.text = if (preferenceManager.isEnabled) {
            "Enabled"
        } else {
            "Disabled"
        }

        binding.toggleButton.text = if (preferenceManager.isEnabled) {
            "Turn Off"
        } else {
            "Turn On"
        }
    }

    private fun startService() {
        val intent = Intent(this, MarqueeLightService::class.java).apply {
            action = MarqueeLightService.ACTION_START
        }
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, MarqueeLightService::class.java).apply {
            action = MarqueeLightService.ACTION_STOP
        }
        startService(intent)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val enabledListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        val expectedComponent = ComponentName(this, com.yanglongss.marqueelight.service.NotificationListenerService::class.java).flattenToString()
        return enabledListeners?.contains(expectedComponent) == true
    }
}
