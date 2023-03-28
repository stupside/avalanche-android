package com.example.avalanche.ui.shared.scaffold

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.ui.theme.AvalancheTheme

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
                button.invoke()
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