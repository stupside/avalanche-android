package com.example.avalanche.ui.components.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun <TData> AvalancheList(
    modifier: Modifier = Modifier,
    elements: List<TData> = emptyList(),
    template: @Composable (data: TData) -> Unit,
) {
    LazyColumn(modifier) {
        for (element in elements.withIndex()) {
            item(element.index, content = {
                template(element.value)
            })
        }
    }
}
