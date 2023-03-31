package com.example.avalanche.core.ui.shared

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

val AvalancheBottomBarActions: List<AvalancheActionConfiguration> = emptyList()

@Composable
fun AvalancheBottomBar(
    actions: List<AvalancheActionConfiguration> = AvalancheBottomBarActions,
    floating: AvalancheActionConfiguration? = null
) {
    BottomAppBar(actions = {
        actions.forEach { action ->
            IconButton(onClick = {
                action.onClick()
            }) {
                Icon(action.icon, contentDescription = action.text)
            }
        }
    }, floatingActionButton = {
        floating?.let {
            AvalancheFloatingActionButton(floating)
        }
    })
}