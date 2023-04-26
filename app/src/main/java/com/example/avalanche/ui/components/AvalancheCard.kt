package com.example.avalanche.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AvalancheCard(
    name: String,
    description: String?,
    logo: String?,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvalancheLogo(logo)
                Spacer(modifier = Modifier.padding(ButtonDefaults.IconSpacing))
                Text(name, style = MaterialTheme.typography.titleMedium)
            }

            description?.let {
                Row {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(it)
                }
            }

        }
    }
}