package com.example.avalanche.views.wallet

import Avalanche.Market.StoreService
import Avalanche.Passport.TicketService
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.TicketActivity
import com.example.avalanche.core.ui.shared.AvalancheActionConfiguration
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
import com.example.avalanche.core.ui.shared.AvalancheFloatingActionButton
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.views.stores.StoreHeader

@Composable
fun WalletView(context: Context, viewModel: WalletViewModel, storeId: String) {

    LaunchedEffect(storeId) {
        try {
            viewModel.loadStore(context, storeId)
            viewModel.loadTickets(context, storeId)
        } catch (_: Exception) {
        }
    }

    val storeState: StoreService.GetStoreProto.Response? by viewModel.store.observeAsState()

    Scaffold(topBar = {
        WalletTopBar(context)
    }, content = { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            storeState?.let { store ->

                StoreHeader(
                    name = store.name,
                    description = store.description,
                    logo = store.logo.toString()
                )

                Column {
                    Text(
                        "Tickets",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )

                    val tickets: List<TicketService.GetTicketsProto.Response> by viewModel.tickets.collectAsState()

                    AvalancheList(elements = tickets, template = { ticket ->
                        WalletTicketItem(
                            context,
                            ticket = ticket.ticketId,
                            name = ticket.name,
                            description = "Ticket description",
                            isValid = ticket.isValidForNow,
                            isSealed = ticket.isSealed
                        )
                    })
                }
            }
        }
    }, floatingActionButton = {
        WalletFloatingActionButton(context, storeId)
    })
}

@Composable
fun WalletTopBar(context: Context) {
    TopAppBar(
        title = {
            Text("Wallet")
        },
        navigationIcon = {
            AvalancheGoBackButton(context)
        },
    )
}

@Composable
fun WalletFloatingActionButton(context: Context, storeId: String) {
    AvalancheFloatingActionButton(AvalancheActionConfiguration(
        Icons.Rounded.Add,
        "Extend or buy a new ticket",
        literal = true,
        onClick = {
            val intent = StoresActivity.getIntent(context, storeId)

            context.startActivity(intent)
        }
    ))
}

@Composable
fun WalletTicketItem(
    context: Context,
    ticket: String,
    name: String,
    description: String,
    isValid: Boolean,
    isSealed: Boolean
) {

    val intent = TicketActivity.getIntent(context, ticket)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(intent)
        }),
        headlineContent = { Text(name) },
        supportingContent = { Text(description) },
        trailingContent = {

            Row {
                AvalancheColoredBadge(isValid, "Valid for now", "Not valid for now")
                Spacer(modifier = Modifier.padding(ButtonDefaults.IconSpacing))
                AvalancheColoredBadge(isSealed, "Sealed", "Unsealed")
            }
        }
    )
}