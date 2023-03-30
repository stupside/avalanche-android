@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.StoreViewModel
import com.example.avalanche.viewmodels.WalletViewModel


class WalletActivity : ComponentActivity() {

    companion object {
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String): Intent {
            return Intent(context, WalletActivity::class.java).putExtra(StoreIdKey, storeId)
        }
    }

    private lateinit var walletVm: WalletViewModel
    private lateinit var storeVm: StoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!

        walletVm = WalletViewModel(storeId)
        storeVm = StoreViewModel(storeId)

        walletVm.loadWallet(this)
        storeVm.loadStore(this)

        setContent {

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Wallet")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        val storeState: StoreService.GetStoreProto.Response? by storeVm.store.observeAsState()

                        storeState?.let { store ->

                            val tickets: List<TicketService.GetTicketsProto.Response> by walletVm.tickets.collectAsState()

                            StoreHeader(
                                context = this@WalletActivity,
                                store = storeId,
                                name = store.name,
                                description = store.description,
                                logo = store.logo.toString()
                            )

                            AvalancheList(elements = tickets, template = { ticket ->
                                TicketItem(
                                    this@WalletActivity,
                                    ticket = ticket.ticketId,
                                    name = ticket.name,
                                    description = "Ticket description"
                                )
                            })
                        }
                    }
                }, floatingActionButton = {
                    ExploreStorePlansButton(context = this, store = storeId)
                })
            }
        }
    }
}

@Composable
fun TicketItem(context: Context, ticket: String, name: String, description: String) {

    val intent = TicketActivity.getIntent(context, ticket)

    ListItem(
        headlineText = { Text(name) },
        supportingText = { Text(description) },
        trailingContent = {
            Button(onClick = {
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = description
                )
            }
        }
    )
}

@Composable
fun ExploreStorePlansButton(context: Context, store: String) {
    val intent = StoreActivity.getIntent(context, store)

    FloatingActionButton(
        onClick = {
            context.startActivity(intent)
        },
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Explore store plans",
            tint = Color.White,
        )
    }
}
