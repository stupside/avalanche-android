package com.example.avalanche.ui.features.wallet.tickets

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.avalanche.ui.components.AvalancheLogo

//Todo: Should have a validity parameter
//It would be represented by a small circle (Green, Red, Orange)
@Composable
fun WalletTicketItem(
    name: String,
    logo: String?,
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
            Text(text = "Validity")
        }
    )

}