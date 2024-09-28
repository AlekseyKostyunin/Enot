package com.alekseykostyunin.enot.tmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MyComposableFunction() {
    var list = listOf("1", "2", "3")
    AutocompleteTextField(list, onSearch = {it.length })
}

@Composable
fun AutocompleteTextField(
    suggestions: List<String>,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { expanded = it.isFocused },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { text = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                    }
                }
            }
        )

        DropdownMenu(
            expanded= expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    onClick = {
                        text = suggestion
                        expanded = false
                    },
                    text = {Text(suggestion)}
                )
            }
        }
    }
}