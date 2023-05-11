package com.example.avalanche.ui.features.wallet.tickets

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avalanche.ui.components.AvalancheLogo

@Composable
fun WalletTicketItem(
    name: String,
    logo: String?,
    valid: Boolean,
    onClick: () -> Unit
) {

    ListItem(
        modifier = Modifier.clickable {
            onClick()
        },
        leadingContent = {
            AvalancheLogo(logo = logo)
        },
        headlineContent = {
            Text(text = name)
        }, trailingContent = {
            if (valid) {

                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                )
            } else {

                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                )
            }
        }
    )

}