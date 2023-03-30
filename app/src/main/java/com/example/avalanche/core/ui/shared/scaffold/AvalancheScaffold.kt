@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche.core.ui.shared.scaffold

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.theme.AvalancheTheme

@Composable
fun AvalancheScaffold(
    activity: ComponentActivity,
    button: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    AvalancheTheme {
        Scaffold(
            topBar = {
                AvalancheScaffoldTopBar(activity)
            },
            floatingActionButton = {
                button()
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}