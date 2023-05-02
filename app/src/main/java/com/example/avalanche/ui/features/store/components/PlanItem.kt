package com.example.avalanche.ui.features.store.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

@Composable
fun PlanItem(
    name: String,
    price: Int,
    free: Boolean,
    duration: Long,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp)) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                style = MaterialTheme.typography.titleMedium,
                text = name
            )

            val days = TimeUnit.SECONDS.toDays(duration)

            val text = if (days == 0L) {
                val minutes = TimeUnit.SECONDS.toMinutes(duration)

                "$minutes minutes"
            } else {
                "$days days"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Duration $text")

                if (free)
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Free"
                    )
                else
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "${price / 100} $"
                    )
            }
        }
    }
}