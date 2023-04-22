package com.example.avalanche.ui.features.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import com.example.avalanche.shared.ui.compose.AvalancheColoredBadge
import com.example.avalanche.shared.ui.compose.AvalancheHeader
import com.example.avalanche.ui.components.AvalancheGoBackButton
import com.example.avalanche.ui.components.list.AvalancheList
import com.example.avalanche.ui.features.ticket.validities.TicketValidityItem

@Composable
fun TicketView(
    viewModel: TicketViewModel,
    ticketId: String,
    goBack: () -> Unit
) {

    LaunchedEffect(ticketId) {
        try {
            viewModel.setTicketId(ticketId)
        } catch (_: Exception) {
        }
    }

    val ticket: GetOneTicketRpc.Response? by viewModel.ticket.observeAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Ticket")
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }, content = { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ticket?.let { ticket ->

                    AvalancheHeader(ticket.name, null, null)

                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                "Validity",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                            )

                            AvalancheColoredBadge(
                                isSuccess = ticket.isValid,
                                successText = "Valid",
                                errorText = "Invalid"
                            )
                        }

                        AvalancheList(elements = ticket.validitiesList, template = { validity ->

                            TicketValidityItem(
                                validity.from.seconds,
                                validity.to.seconds,
                                validity.kind
                            )
                        })
                    }
                }
            }
        }
    })
}

