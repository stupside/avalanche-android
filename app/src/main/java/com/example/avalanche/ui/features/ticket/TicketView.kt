package com.example.avalanche.ui.features.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
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
import avalanche.merchant.store.Store.GetOneStoreRpc
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton
import com.example.avalanche.ui.components.AvalancheLogo
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
    val store: GetOneStoreRpc.Response? by viewModel.store.observeAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            ticket?.let {
                Text(it.name)
            }
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }, content = { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                store?.let {
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {

                        Row(modifier = Modifier.padding(8.dp)) {

                            AvalancheLogo(logo = it.logo.value)

                            Column {

                                Text(it.name, style = MaterialTheme.typography.titleLarge)
                                Text(it.description, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }

                ticket?.let { ticket ->

                    LazyColumn {

                        for (element in ticket.validitiesList.withIndex()) {

                            item(element.index) {
                                val validity = element.value
                                TicketValidityItem(
                                    from = validity.from.seconds,
                                    to = validity.to.seconds,
                                    kind = validity.kind,
                                    span = validity.span.seconds
                                )
                            }
                        }
                    }
                }
            }
        }
    })
}

