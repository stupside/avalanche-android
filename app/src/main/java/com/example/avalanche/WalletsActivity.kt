@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.WalletsViewModel
import com.google.android.material.chip.Chip

//
class WalletsActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WalletsActivity::class.java)
        }
    }

    private val walletsVm: WalletsViewModel by lazy {
        WalletsViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        walletsVm.loadWallets(this)

        setContent {

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Wallets")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        val wallets: List<TicketService.GetWalletsProto.Response> by walletsVm.wallets.collectAsState()

                        AvalancheList(elements = wallets, template = { wallet ->
                            WalletItem(
                                this@WalletsActivity,
                                wallet.storeId,
                                wallet.storeId,
                                "Wallet description",
                                wallet.ticketCount,
                                Icons.Filled.AccountBox
                            )
                        })
                    }
                }, floatingActionButton = {
                    ExploreStoresButton(this)
                })
            }
        }
    }
}

@Composable
fun WalletItem(
    context: Context,
    wallet: String,
    name: String,
    description: String,
    tickets: Int,
    logo: ImageVector
) {

    val intent = WalletActivity.getIntent(context, wallet)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(intent)
        }),
        headlineText = { Text(name) },
        leadingContent = {
            Icon(logo, contentDescription = description)
        },
        trailingContent = {
            Text("$tickets tickets")
        },
        supportingText = { Text(description) }
    )
}

@Composable
fun ExploreStoresButton(context: Context) {
    val intent = StoresActivity.getIntent(context)

    FloatingActionButton(
        onClick = {
            context.startActivity(intent)
        },
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Explore store",
        )
    }
}