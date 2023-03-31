package com.example.avalanche.core.ui.shared

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.avalanche.WalletsActivity

class AvalancheBottomBarAction(
    val icon: ImageVector,
    val description: String,
    val onClick: (context: Context) -> Unit,
)

val AvalancheBottomBarActions: List<AvalancheBottomBarAction> = listOf(
    AvalancheBottomBarAction(Icons.Filled.Home, "Home") { context ->
        context.startActivity(WalletsActivity.getIntent(context))
    },
    AvalancheBottomBarAction(Icons.Filled.Face, "Identity") { context ->
        context.startActivity(WalletsActivity.getIntent(context))
    },
)

@Composable
fun AvalancheBottomBar(
    context: Context,
    actions: List<AvalancheBottomBarAction> = AvalancheBottomBarActions,
    floating: AvalancheBottomBarAction?
) {

    BottomAppBar(actions = {
        actions.forEach { action ->
            IconButton(onClick = {
                action.onClick(context)
            }) {
                Icon(action.icon, contentDescription = action.description)
            }
        }
    }, floatingActionButton = {
        floating?.let {
            FloatingActionButton(
                onClick = {
                    floating.onClick(context)
                },
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = floating.icon,
                    contentDescription = floating.description,
                )
            }
        }
    })
}