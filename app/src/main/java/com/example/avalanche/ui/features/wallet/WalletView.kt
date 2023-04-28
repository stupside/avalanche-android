package com.example.avalanche.ui.features.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.vault.ticket.Ticket
import com.example.avalanche.ui.features.wallet.tickets.WalletTicketItem

@Composable
fun WalletView(
    viewModel: WalletViewModel,
    goStores: () -> Unit,
    goTicket: (ticketId: String) -> Unit
) {

    SideEffect {
        viewModel.loadTickets()
    }

    val tickets: Ticket.GetManyTicketsRpc.Response? by viewModel.tickets.observeAsState()

    Scaffold(topBar = {

        TopAppBar(title = {
            Text("Wallet")
        })
    }, content = { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                tickets?.let {

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        for (ticket in it.itemsList) {

                            item(ticket.ticketId) {

                                WalletTicketItem(
                                    // Todo: Should be station name
                                    name = ticket.name,
                                    // Todo: Replace id with more important information
                                    //  (Ticket name)
                                    // description = ticket.ticketId,
                                    description = "Ticket Name",
                                    logo = null,
                                    // Todo: Should have a validty parameter
                                    onClick = {
                                        goTicket(ticket.ticketId)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }, floatingActionButton = {

        ExtendedFloatingActionButton(text = {
            Text("Explore stores")
        }, icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
            )
        }, onClick = {
            goStores()
        })
    })
}