package com.example.avalanche.ui.features.wallet.tickets

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
            Text(
                fontWeight = FontWeight.SemiBold,
                text = name
            )
        }, trailingContent = {
            if (valid) {

                Icon(
                    Icons.Sharp.CheckCircle,
                    contentDescription = null,
                )
            } else {

                Icon(
                    Icons.Sharp.Warning,
                    contentDescription = null,
                )
            }
        }
    )

}