package com.example.avalanche.core.ui.shared.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun <TData> AvalancheList(
    elements: List<TData> = emptyList(),
    template: @Composable (data: TData) -> Unit,
) {
    LazyColumn {
        for (element in elements.withIndex()) {

            item(element.index, content = {

                Box(modifier = Modifier.padding(16.dp, 8.dp)){
                    template(element.value)
                }
            })
        }
    }
}
