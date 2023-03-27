package com.example.avalanche.ui.shared.pass

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun <PData> AvalanchePassInfo(
    performances: List<PData> = emptyList(),
    Current: Boolean,
    general_content: @Composable () -> Unit,
    validity_content: @Composable () -> Unit,
    time_left: @Composable () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Pass Info")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            general_content()
        }
        //Si l'activité est celle d'un pass en utilisation au moment présent
        if (Current) {
            Column() {
                Text(text = "Time left")
                time_left()
            }
            Row() {
                Text(text = "Performance")

                //Code pour faire en sorte que le switch ai un check lorsqu'il est checked
                var checked by remember { mutableStateOf(true) }
                // Icon isn't focusable, no need for content description
                val icon: (@Composable () -> Unit)? = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }

                //Code pour le switch en lui-même
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Demo with icon" },
                    checked = checked,
                    onCheckedChange = { checked = it },
                    thumbContent = icon
                )

                //Si le Switch est checked alors il doit afficher les performances de l'utilisateur
                if (checked) {
                    for (element in performances.withIndex()) {
                        element.value
                    }
                }
            }
        }
        //Si l'activité n'est pas active à ce moment donnée
        else{
            Column() {
                Text(text = "Period of validity")
                validity_content()
            }
        }
    }
}