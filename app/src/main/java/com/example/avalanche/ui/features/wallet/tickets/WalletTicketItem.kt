package com.example.avalanche.ui.features.wallet.tickets

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WalletTicketItem(
    name: String,
    onClick: () -> Unit
) {

    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(name) }
    )
}