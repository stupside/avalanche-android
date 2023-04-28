package com.example.avalanche.ui.features.wallet.tickets

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avalanche.ui.components.AvalancheLogo

//Todo: Should have a validity parameter
//It would be represented by a small circle (Green, Red, Orange)
@Composable
fun WalletTicketItem(
    name: String,
    description: String,
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
            Text(name)
        }, supportingContent = {
            Text(description)
        }, trailingContent = {
            Text(text = "Validity")
        }
        )

}