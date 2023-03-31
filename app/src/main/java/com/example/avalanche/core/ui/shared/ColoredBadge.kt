package com.example.avalanche.core.ui.shared

import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AvalancheColoredBadge(isSuccess: Boolean, successText: String?, errorText: String?) {

    val text = if (isSuccess) {
        successText
    } else {
        errorText
    }

    text?.let {
        Badge(
            containerColor = if (isSuccess) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            },
            contentColor = if (isSuccess) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            }
        ) {
            Text(text)
        }
    }
}