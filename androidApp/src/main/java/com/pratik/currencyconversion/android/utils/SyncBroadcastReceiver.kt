package com.pratik.currencyconversion.android.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SyncBroadcastReceiver : BroadcastReceiver() {
    private val helper: SyncHelper by lazy { SyncHelper() }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SyncBroadcastReceiver", "receive intent")
        helper.onReceive()
    }
}