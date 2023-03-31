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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.avalanche.core.ui.shared.AvalancheBottomBar
import com.example.avalanche.core.ui.shared.AvalancheBottomBarAction
import com.example.avalanche.core.ui.shared.AvalancheColoredBadge
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.WalletsViewModel

//
class WalletsActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WalletsActivity::class.java)
        }
    }

    private lateinit var walletsVm: WalletsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        walletsVm = WalletsViewModel()

        setContent {

            val wallets: List<TicketService.GetWalletsProto.Response> by walletsVm.wallets.collectAsState()

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Wallets")
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

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

                }, bottomBar = {
                    AvalancheBottomBar(
                        this,
                        floating = AvalancheBottomBarAction(Icons.Rounded.Add, "Explore stores") {
                            startActivity(StoresActivity.getIntent(this@WalletsActivity))
                        })
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        walletsVm.loadWallets(this)
    }
}

@Composable
fun WalletItem(
    context: Context,
    walletId: String,
    walletName: String,
    description: String,
    ticketCount: Int,
    logo: ImageVector
) {

    val intent = WalletActivity.getIntent(context, walletId)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(intent)
        }),
        headlineContent = { Text(walletName) },
        leadingContent = {
            Icon(logo, contentDescription = description)
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