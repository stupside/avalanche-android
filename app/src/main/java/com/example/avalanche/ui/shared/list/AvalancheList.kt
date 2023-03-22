package com.example.avalanche.ui.shared.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun <TData> AvalancheList(
    elements: List<TData> = emptyList(),
    template: @Composable (data: TData) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (element in elements.withIndex()) {
            item(element.index, content = {
                template(element.value)
            })
        }
    }
}
