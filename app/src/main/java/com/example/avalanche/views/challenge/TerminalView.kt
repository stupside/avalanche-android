package com.example.avalanche.views.challenge

import Avalanche.Market.StoreService.GetStoreProto
import Avalanche.Passport.TicketService.GetTicketProto
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.views.stores.StoreHeader
import com.example.avalanche.views.ticket.TicketHeader

@Composable
fun TerminalView(context: Context, viewModel: TerminalViewModel) {

    val storeId = Constants.getSharedPreferences(context)?.getString(Constants.AVALANCHE_SHARED_PREFERENCES_STORE, Constants.STORE_ID)

    val deviceIdentifier = Constants.DEVICE_IDENTIFIER

    LaunchedEffect(storeId, deviceIdentifier) {
        storeId?.let {
            viewModel.loadStore(context, it)
            viewModel.loadTicket(context, it, deviceIdentifier)
        }
    }

    val store: GetStoreProto.Response? by viewModel.store.observeAsState()
    val ticket: GetTicketProto.Response? by viewModel.ticket.observeAsState()

    Scaffold(topBar = {
        TerminalTopBar(context)
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

                if(store == null){
                    Text("Please make sure to setup an active ticket from your wallets", style = MaterialTheme.typography.titleSmall)
                }

                store?.let {

                    StoreHeader(
                        name = it.name,
                        description = it.description,
                        logo = it.logo.toString()
                    )

                    if (ticket == null) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No ticket sealed for this store",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedButton(onClick = {
                                val storesIntent = StoresActivity.getIntent(context, storeId)

                                context.startActivity(storesIntent)
                            }) {
                                Text("Buy a ticket now")
                            }
                        }
                    }

                    ticket?.let { ticket ->
                        TicketHeader(ticketName = ticket.name)
                    }
                }
            }
        }
    })
}

@Composable
fun TerminalTopBar(context: Context) {
    TopAppBar(title = {
        Text("Terminal")
    }, navigationIcon = {
        AvalancheGoBackButton(context)
    })
}