package com.example.avalanche.ui.drm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.drm.auth.Auth.AcquireChallengeRpc
import avalanche.merchant.store.Store.GetOneStoreRpc
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton

@Composable
fun WriterView(viewModel: WriterViewModel, goBack: () -> Unit, challengeId: String?) {

    val flow: AcquireChallengeRpc.Response? by viewModel.flow.collectAsState()

    val ticket: GetOneTicketRpc.Response? by viewModel.ticket.observeAsState()
    val store: GetOneStoreRpc.Response? by viewModel.store.observeAsState()

    LaunchedEffect(challengeId) {

        challengeId?.let {
            viewModel.challenge(it)
        }
    }

    Scaffold(topBar = {

        TopAppBar(title = {

            Text("Writer")
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {

                challengeId?.let {

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        Text("Challenge", style = MaterialTheme.typography.titleMedium)

                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                flow?.let {

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Validation", style = MaterialTheme.typography.titleMedium)

                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(it.message.value, style = MaterialTheme.typography.bodyMedium)
                            Text(it.success.toString(), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                ticket?.let {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text("Ticket", style = MaterialTheme.typography.titleMedium)

                        Text(it.ticketId, style = MaterialTheme.typography.bodySmall)
                    }
                }

                store?.let {

                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text("Store", style = MaterialTheme.typography.titleMedium)

                        Text(it.name, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}