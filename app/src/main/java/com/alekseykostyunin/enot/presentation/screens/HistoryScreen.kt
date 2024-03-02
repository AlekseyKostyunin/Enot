package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults

@Preview
@Composable
fun HistoryScreen(){
    val Item1 = "Первая"
    val Item2 = "Вторая"
    val Item3 = "Третья"
    val items = remember { mutableListOf(Item1, Item2, Item3) } // Any type of items

    JetLimeColumn(
        modifier = Modifier.padding(16.dp),
        itemsList = ItemsList(items),
        //key = { _, item -> item.id },
    ) { index, item, position ->
        JetLimeEvent(
            style = JetLimeEventDefaults.eventStyle(
                position = position
            ),
        ) {
            // Content here
        }
    }
}