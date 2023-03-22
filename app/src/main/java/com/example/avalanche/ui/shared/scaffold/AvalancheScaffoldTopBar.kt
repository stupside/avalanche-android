package com.example.avalanche.ui.shared.scaffold

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvalancheScaffoldTopBar(activity: ComponentActivity) {

    val title = activity.title.toString();

    return MediumTopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 34.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { activity.onBackPressedDispatcher.onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
    )
}