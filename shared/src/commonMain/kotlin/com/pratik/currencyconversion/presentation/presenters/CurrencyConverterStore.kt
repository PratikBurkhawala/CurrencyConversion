package com.pratik.currencyconversion.presentation.presenters

import com.pratik.currencyconversion.domain.models.CurrencyRate
import com.pratik.currencyconversion.domain.models.RepositoryResult
import com.pratik.currencyconversion.domain.repository.Repository
import com.pratik.currencyconversion.presentation.presenters.models.ConvertedCurrencyRate
import com.pratik.currencyconversion.presentation.presenters.utils.convertToTargetCurrencyRate
import com.pratik.currencyconversion.presentation.source.Action
import com.pratik.currencyconversion.presentation.source.Effect
import com.pratik.currencyconversion.presentation.source.State
import com.pratik.currencyconversion.presentation.source.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ScreenState(
    val isLoading: Boolean = false,
    val editFieldText: String = "",
    val availableOptions: List<String> = emptyList(),
    val selectedCurrency: String = "",
    val convertedRates: List<ConvertedCurrencyRate> = emptyList()
) : State

sealed class ScreenAction : Action {
    object OnInit : ScreenAction()
    object JustSync : ScreenAction()
    data class OnEditFieldChange(val value: String) : ScreenAction()
    data class OnSelectedCurrencyChange(val code: String) : ScreenAction()
}

sealed class ScreenSideEffect : Effect {
    data class Error(val errorMessage: String) : ScreenSideEffect()
}

class CurrencyConverterStore(
    private val repository: Repository
) : Store<ScreenState, ScreenAction, ScreenSideEffect>, CoroutineScope by CoroutineScope(
    Dispatchers.Main
) {

    private val state = MutableStateFlow(ScreenState())
    private val sideEffect = MutableSharedFlow<ScreenSideEffect>()

    private var listOfCurrencies: List<CurrencyRate>? = null

    override fun observeState(): StateFlow<ScreenState> = state

    override fun observeEffect(): Flow<ScreenSideEffect> = sideEffect

    override fun dispatch(action: ScreenAction) {
        when (action) {
            is ScreenAction.OnInit -> {
                launch {
                    state.value = state.value.copy(
                        isLoading = true
                    )
                    getListOfCurrencies()
                }
            }
            is ScreenAction.OnEditFieldChange -> {
                state.value = state.value.copy(
                    editFieldText = action.value
                )
                if (state.value.selectedCurrency.isNotEmpty()) {
                    launch {
                        state.value = state.value.copy(
                            convertedRates = createUpdatedState()
                        )
                    }
                }
            }
            is ScreenAction.OnSelectedCurrencyChange -> {
                state.value = state.value.copy(
                    selectedCurrency = action.code
                )
                if (state.value.editFieldText.isNotEmpty()) {
                    launch {
                        state.value = state.value.copy(
                            convertedRates = createUpdatedState()
                        )
                    }
                }
            }

            is ScreenAction.JustSync -> {
                launch {
                    repository.refresh()
                }
            }
        }
    }

    private suspend fun getListOfCurrencies() = withContext(Dispatchers.Default) {
        repository.getAllRates().collect { result ->
            when (result) {
                is RepositoryResult.Success -> {
                    listOfCurrencies = result.data
                    listOfCurrencies?.map { it.code }?.let { newOptions ->
                        state.value =
                            if (state.value.editFieldText != "" && state.value.selectedCurrency != "") {
                                state.value.copy(
                                    isLoading = false,
                                    availableOptions = newOptions,
                                    convertedRates = createUpdatedState()
                                )
                            } else {
                                state.value.copy(
                                    isLoading = false,
                                    availableOptions = newOptions
                                )
                            }

                    }
                }
                is RepositoryResult.Error -> {
                    state.value = state.value.copy(
                        isLoading = false
                    )
                    result.errorMessage?.let { ScreenSideEffect.Error(it) }
                        ?.let { sideEffect.emit(it) }
                }
            }
        }
    }

    private fun createUpdatedState(): List<ConvertedCurrencyRate> {
        val newList = mutableListOf<ConvertedCurrencyRate>()
        val currentCurrencyRate =
            listOfCurrencies?.first { it.code == state.value.selectedCurrency }?.rate ?: 0.0
        listOfCurrencies?.forEach { rate ->
            newList.add(
                ConvertedCurrencyRate(
                    rate.code,
                    convertToTargetCurrencyRate(
                        currentCurrencyRate,
                        state.value.editFieldText.toDoubleOrNull() ?: 0.0,
                        rate.rate
                    )
                )
            )
        }
        return newList
    }
}