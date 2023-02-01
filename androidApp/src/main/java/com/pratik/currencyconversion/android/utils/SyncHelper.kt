package com.pratik.currencyconversion.android.utils

import android.util.Log
import com.pratik.currencyconversion.presentation.presenters.CurrencyConverterStore
import com.pratik.currencyconversion.presentation.presenters.ScreenAction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncHelper : KoinComponent {
    private val store: CurrencyConverterStore by inject()
    fun onReceive() {
        Log.d("SyncBroadcastReceiver", "receive in helper")
        store.dispatch(ScreenAction.JustSync)
    }
}