package com.pratik.currencyconversion.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratik.currencyconversion.android.R
import com.pratik.currencyconversion.android.ui.components.CurrencyBlock
import com.pratik.currencyconversion.android.ui.components.EditableDropdown
import com.pratik.currencyconversion.presentation.presenters.ScreenState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: ScreenState,
    onTextFieldUpdate: (String) -> Unit,
    onSelectedCurrencyChange: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            TextField(
                value = state.editFieldText,
                onValueChange = { newText ->
                    onTextFieldUpdate(newText)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.text_field_placeholder)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            EditableDropdown(
                selectedCurrencyCode = state.selectedCurrency,
                listItems = state.availableOptions,
                onSelectedCurrencyChange = { newCode ->
                    onSelectedCurrencyChange(newCode)
                })

            Spacer(modifier = Modifier.padding(8.dp))

            Box(modifier = Modifier.fillMaxHeight(1f)) {
                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    if (state.convertedRates.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = stringResource(id = R.string.help_text),
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.Center,
                            columns = GridCells.Fixed(4),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.convertedRates) { item ->
                                CurrencyBlock(content = {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center)
                                    ) {
                                        Text(
                                            text = item.convertedRate,
                                            color = MaterialTheme.colors.onSurface,
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Text(
                                            text = item.currencyCode,
                                            color = MaterialTheme.colors.onSurface,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}