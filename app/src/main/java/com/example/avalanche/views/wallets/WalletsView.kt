package com.example.avalanche.views.wallets

import Avalanche.Passport.TicketService
import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.WalletActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
import com.example.avalanche.core.ui.shared.AvalancheLogo
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.views.challenge.DiscoveryActivity

@Composable
@ExperimentalGetImage
fun WalletsView(context: Context, viewModel: WalletsViewModel) {

    SideEffect {
        try {
            viewModel.loadWallets(context)
        } catch (_: Exception) {
        }
    }

    val deviceIdentifier = Constants.DEVICE_IDENTIFIER

    val preferences = Constants.getSharedPreferences(context)

    val storeId by remember {
        mutableStateOf(
            preferences?.getString(
                Constants.AVALANCHE_SHARED_PREFERENCES_STORE,
                Constants.STORE_ID
            )
        )
    }

    LaunchedEffect(storeId) {
        storeId?.let {
            // viewModel.loadTicket(context, it, deviceIdentifier)
        }
    }

    val wallets: List<TicketService.GetWalletsProto.Response> by viewModel.wallets.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Wallets")
        })
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 32.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        val ticket: TicketService.GetTicketProto.Response? by viewModel.ticket.observeAsState()

                        ticket?.let {
                            Text("Your active ticket is '${it.name}'")
                        }

                        Text(
                            "Change your ticket for another store",
                            modifier = Modifier.clickable(onClick = {
                                val intent = DiscoveryActivity.getIntent(context)
                                context.startActivity(intent)
                            })
                        )
                    }
                }

                AvalancheList(elements = wallets, template = { wallet ->
                    WalletItem(
                        context,
                        wallet.storeId,
                        wallet.storeId,
                        "Wallet description",
                        wallet.ticketCount,
                        null,
                    )
                })

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    WalletExploreStores(context)
                }
            }
        }
    })
}

@Composable
fun WalletExploreStores(context: Context) {
    Text(
        "Search a store, create or extend your tickets",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.clickable(onClick = {
            val stores = StoresActivity.getIntent(context)

            context.startActivity(stores)
        })
    )
}

@Composable
fun WalletItem(
    context: Context,
    storeId: String,
    walletName: String,
    description: String,
    ticketCount: Int,
    logo: String?
) {

    val intent = WalletActivity.getIntent(context, storeId)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(intent)
        }),
        headlineContent = { Text(walletName) },
        leadingContent = {
            AvalancheLogo(logo)
        },
        trailingContent = {
            AvalancheColoredBadge(
                isSuccess = ticketCount > 0,
                successText = "$ticketCount tickets",
                errorText = null
            )
        },
        supportingContent = { Text(description) },
    )
}