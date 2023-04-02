package com.example.avalanche.views.nfc

import Avalanche.Market.StoreService.GetStoreProto
import Avalanche.Passport.TicketService.GetTicketProto
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.views.stores.StoreHeader
import com.example.avalanche.views.ticket.TicketTopBar

@Composable
fun NfcView(context: Context, viewModel: NfcViewModel, storeId: String) {
    LaunchedEffect(storeId) {
        viewModel.loadStore(context, storeId)
        viewModel.loadTicket(context, storeId)
    }

    val store: GetStoreProto.Response? by viewModel.store.observeAsState()
    val ticket: GetTicketProto.Response? by viewModel.ticket.observeAsState()

    Scaffold(topBar = {
        TicketTopBar(context)
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

                store?.let {
                    StoreHeader(
                        name = it.name,
                        description = it.description,
                        logo = it.logo.toString()
                    )
                }

                store?.let {
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
                                Text("Buy now")
                            }
                        }
                    } else {
                        Text("Show info of ticket ${ticket?.name}")
                        // TODO: grpc challenge and show if valid on screen
                        // TODO: on wallets, show the current ticket in use, regardless of the current station
                        // TODO: Session.Track with a background service
                    }
                }
            }
        }
    })
}