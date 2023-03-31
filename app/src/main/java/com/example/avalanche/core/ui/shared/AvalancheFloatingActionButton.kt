package com.example.avalanche.core.ui.shared

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

class AvalancheActionConfiguration(
    val icon: ImageVector,
    val text: String,
    val literal: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
fun AvalancheFloatingActionButton(
    action: AvalancheActionConfiguration
) {
    if (action.literal) {
        ExtendedFloatingActionButton(
            text = {
                Text(action.text)
            },
            icon = {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.text,
                )
            },
            onClick = {
                action.onClick()
            },
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        )
    } else {
        FloatingActionButton(
            onClick = {
                action.onClick()
            },
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.text,
            )
        }
    }
}