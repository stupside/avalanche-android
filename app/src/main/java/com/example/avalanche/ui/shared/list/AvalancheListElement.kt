package com.example.avalanche.ui.shared.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AvalancheListElement(
    content: @Composable () -> Unit,
    onClick: (() -> Unit)?
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = {
            onClick?.invoke()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            content()
        }
    }
}