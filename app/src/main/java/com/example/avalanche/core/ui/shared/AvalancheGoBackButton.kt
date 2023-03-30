package com.example.avalanche.core.ui.shared

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun AvalancheGoBackButton(activity: ComponentActivity) {

    IconButton(onClick = { activity.finish() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Go back"
        )
    }
}