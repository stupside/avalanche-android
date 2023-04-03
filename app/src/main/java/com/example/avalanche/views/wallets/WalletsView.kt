package com.example.avalanche.views.wallets

import Avalanche.Passport.TicketService
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.avalanche.R
import com.example.avalanche.StoresActivity
import com.example.avalanche.WalletActivity
import com.example.avalanche.core.ui.shared.AvalancheActionConfiguration
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
import com.example.avalanche.core.ui.shared.AvalancheFloatingActionButton
import com.example.avalanche.core.ui.shared.AvalancheLogo
import com.example.avalanche.core.ui.shared.list.AvalancheList
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletsView(context: Context, viewModel: WalletsViewModel) {
    SideEffect {
        try {
            viewModel.loadWallets(context)
        } catch (_: Exception) {
        }
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
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedCard(modifier = Modifier) {

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

                if (showDialog) {
                    AlertDialog({ showDialog = false }) {
                        Surface(
                            //modifier = Modifier.wrapContentSize(),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Actions",
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    IconButton(onClick = {
                                        showDialog = false
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(32.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    WalletDialogListItem_ExploreStore(context) {
                                        showDialog = false
                                    }
                                }
                            }
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
        headlineContent = {
            Text("Search a store, create and extend your tickets")
        },
        trailingContent = {
            Icon(Icons.Default.Search, contentDescription = null)
        }
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