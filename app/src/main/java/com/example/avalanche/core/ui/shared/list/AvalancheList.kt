package com.example.avalanche.core.ui.shared.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable


@Composable
fun <TData> AvalancheList(
    elements: List<TData> = emptyList(),
    template: @Composable (data: TData) -> Unit,
) {
    LazyColumn {
        for (element in elements.withIndex()) {
            item(element.index, content = {
                Box{
                    template(element.value)
                }
            })
        }
    }
}
