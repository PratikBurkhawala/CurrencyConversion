package com.pratik.currencyconversion.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.pratik.currencyconversion.android.R
import com.pratik.currencyconversion.android.ui.components.ComposableLifecycle
import com.pratik.currencyconversion.android.ui.screens.MainScreen
import com.pratik.currencyconversion.presentation.presenters.CurrencyConverterStore
import com.pratik.currencyconversion.presentation.presenters.ScreenAction
import com.pratik.currencyconversion.presentation.presenters.ScreenSideEffect
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@Composable
fun App(
    store: CurrencyConverterStore = get()
) {

    val scaffoldState = rememberScaffoldState()
    val error = store.observeEffect()
    val state by store.observeState().collectAsState()
    val actionLabel = stringResource(id = R.string.snackbar_button_label)

    ComposableLifecycle(onEvent = { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            store.dispatch(ScreenAction.OnInit)
        }
    })

    LaunchedEffect(true) {
        coroutineScope {
            error.collectLatest { sideEffect ->
                when (sideEffect) {
                    is ScreenSideEffect.Error -> {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            sideEffect.errorMessage,
                            actionLabel = actionLabel,
                            duration = SnackbarDuration.Indefinite
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                store.dispatch(ScreenAction.OnInit)
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) { paddingValue ->
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            state = state,
            onTextFieldUpdate = { newText ->
                store.dispatch(ScreenAction.OnEditFieldChange(newText))
            },
            onSelectedCurrencyChange = { newCode ->
                store.dispatch(ScreenAction.OnSelectedCurrencyChange(newCode))
            }
        )
    }
}