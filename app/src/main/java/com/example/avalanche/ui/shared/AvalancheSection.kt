package com.example.avalanche.ui.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun AvalancheSection(
    title: String,
    content: @Composable () -> Unit
) {
    Row {

        Text(
            title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 34.sp
        )

        content()
    }
}
