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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.drm.auth.Auth.WatchChallengeRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton

@Composable
fun ReaderView(
    viewModel: ReaderViewModel,
    goBack: () -> Unit,
    isTagEnabled: Boolean,
    setupTag: (prepareChallenge: (storeId: String) -> String, watchChallenge: (challengeId: String) -> Unit) -> Unit,
) {

    val challengeId: String? by viewModel.challengeId.observeAsState()

    val flow: WatchChallengeRpc.Response? by viewModel.flow.collectAsState()

    SideEffect {
        setupTag({
            viewModel.prepareChallenge(it)
        }, {
            viewModel.watchChallenge(it)
        })
    }

    Scaffold(topBar = {

        TopAppBar(title = {

            Text("Reader (tag: $isTagEnabled)")
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(it.message.value, style = MaterialTheme.typography.bodyMedium)
                            Text(it.success.toString(), style = MaterialTheme.typography.bodySmall)
                        }

                        Text("Ticket: ${it.ticketId.value}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}