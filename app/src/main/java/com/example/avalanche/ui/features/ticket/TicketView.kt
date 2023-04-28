package com.example.avalanche.ui.features.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    goBack: () -> Unit,
    goStore: (storeId: String) -> Unit
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
                Text(text = it.name)
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



                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column (
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ListItem(
                            headlineContent = {
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "Resort"
                            )
                        }, supportingContent = {
                            store?.let {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    text = it.name
                                )
                            }
                        })

                        ListItem(
                            headlineContent = {
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "Ticket Id"
                            )
                        }, supportingContent = {
                            ticket?.let {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    text = it.ticketId
                                )
                            }
                        })

                        Text(
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.SemiBold,
                            text = "Dates when this ticket can be used"
                        )

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

                        store?.let {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {

                                FilledTonalButton(onClick = {
                                    goStore(it.storeId)
                                }) {
                                    Text("Extend this ticket")
                                }
                            }
                        }
                    }
                }

                ElevatedCard(modifier = Modifier.fillMaxWidth()) {

                    store?.let {

                        Column (
                            modifier = Modifier.padding(32.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                AvalancheLogo(logo = it.logo.value)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "About ${it.name}",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(end = 50.dp)
                                )
                            }

                            Text(it.description, style = MaterialTheme.typography.bodyLarge)

                            TextButton(onClick = { /*TODO*/ }) {
                                Text(
                                    modifier = Modifier.padding(end = 8.dp),
                                    text = "Resort Website"
                                )
                                Icon(
                                    Icons.Rounded.ArrowForward,
                                    contentDescription = "Go to the Resort's website"
                                )
                            }
                        }
                    }
                }
            }
        }
    })
}

