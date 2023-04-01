package com.example.avalanche.core.ui.shared

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun AvalancheGoBackButton(context: Context) {

    IconButton(onClick = { (context as ComponentActivity).finish() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Go back"
        )
    }
}