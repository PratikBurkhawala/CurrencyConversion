package com.pratik.currencyconversion.android.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.pratik.currencyconversion.android.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditableDropdown(
    listItems: List<String> = emptyList(),
    selectedCurrencyCode: String,
    onSelectedCurrencyChange: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            value = selectedCurrencyCode,
            onValueChange = {
            },
            label = { Text(text = stringResource(id = com.pratik.currencyconversion.android.R.string.dropdown_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onSelectedCurrencyChange(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}