package com.example.avalanche.views.wallets

import Avalanche.Passport.TicketService
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.WalletActivity
import com.example.avalanche.core.ui.shared.AvalancheActionConfiguration
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
import com.example.avalanche.core.ui.shared.AvalancheFloatingActionButton
import com.example.avalanche.core.ui.shared.AvalancheLogo
import com.example.avalanche.core.ui.shared.list.AvalancheList

@Composable
fun WalletsView(context: Context, viewModel: WalletsViewModel) {

    try {
        viewModel.loadWallets(context)
    } catch (_: Exception) {
    }

    val wallets: List<TicketService.GetWalletsProto.Response> by viewModel.wallets.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        WalletsTopBar()
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

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

            if (showDialog) {
                AlertDialog({ showDialog = false }) {
                    Surface(
                        modifier = Modifier.wrapContentSize(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("Actions", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                            WalletDialogListItem_ExploreStore(context) { showDialog = false }
                        }
                    }
                }
            }
        }
    }, floatingActionButton = {
        WalletFloatingBar {
            showDialog = true
        }
    })
}

@Composable
fun WalletsTopBar() {
    TopAppBar(title = {
        Text("Wallets")
    })
}

@Composable
fun WalletFloatingBar(onClick: () -> Unit) {
    AvalancheFloatingActionButton(
        AvalancheActionConfiguration(
            Icons.Rounded.Add,
            "Manage wallets",
            literal = true,
            onClick = onClick
        )
    )
}

@Composable
fun WalletDialogListItem_ExploreStore(context: Context, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = {

            onClick()

            val stores = StoresActivity.getIntent(context)

            context.startActivity(stores)
        }),
        leadingContent = {
            Icon(Icons.Default.Menu, contentDescription = null)
        },
        headlineContent = {
            Text("Explore stores")
        }, supportingContent = {
            Text("Search a stores, create and extend your tickets")
        })
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