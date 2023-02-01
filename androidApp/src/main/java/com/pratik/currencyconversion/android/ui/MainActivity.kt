package com.pratik.currencyconversion.android.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.pratik.currencyconversion.android.ui.theme.MyApplicationTheme
import com.pratik.currencyconversion.android.utils.SyncBroadcastReceiver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleSyncUpdates()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }

    private fun scheduleSyncUpdates() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SyncBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmToTriggerInSeconds = 1800
        val alarmTimeAtUTC = System.currentTimeMillis() + alarmToTriggerInSeconds * 1_000L
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC,
            alarmToTriggerInSeconds * 1_000L,
            pendingIntent
        )
    }
}