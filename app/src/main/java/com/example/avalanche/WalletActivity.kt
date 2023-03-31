@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.shared.AvalancheBottomBar
import com.example.avalanche.core.ui.shared.AvalancheBottomBarAction
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
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

        setContent {

            val storeState: StoreService.GetStoreProto.Response? by storeVm.store.observeAsState()

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text("Wallet")
                        },
                        navigationIcon = {
                            AvalancheGoBackButton(activity = this)
                        },
                    )
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        storeState?.let { store ->

                            StoreHeader(
                                name = store.name,
                                description = store.description,
                                logo = store.logo.toString()
                            )

                            Column {
                                Text("Tickets", style = MaterialTheme.typography.titleMedium)

                                val tickets: List<TicketService.GetTicketsProto.Response> by walletVm.tickets.collectAsState()

                                AvalancheList(elements = tickets, template = { ticket ->
                                    TicketItem(
                                        this@WalletActivity,
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
                }, bottomBar = {
                    AvalancheBottomBar(
                        this,
                        floating = AvalancheBottomBarAction(
                            Icons.Rounded.Add,
                            "Explore store plans"
                        ) {
                            startActivity(StoreActivity.getIntent(this@WalletActivity, storeId))
                        })
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        walletVm.loadWallet(this)
        storeVm.loadStore(this)
    }
}

@Composable
fun TicketItem(
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
