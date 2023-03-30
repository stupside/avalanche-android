package com.example.avalanche

import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.list.AvalancheList
import com.example.avalanche.ui.shared.list.AvalancheListElement
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.WalletsViewModel

//
class WalletsActivity : ComponentActivity() {

    private val vmWallets: WalletsViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WalletsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmWallets.loadWallets(this)

        setContent {

            AvalancheScaffold(
                activity = this,
                content = {
                    AvalancheSection(title = "List of Station Id's") {

                        val wallets: List<TicketService.GetWalletsProto.Response> by vmWallets.data.observeAsState(
                            emptyList()
                        )

                        AvalancheList(elements = wallets, template = { wallet ->

                            val store = wallet.storeId

                            val tickets = wallet.ticketCount.toString()

                            AvalancheListElement(
                                onClick = {
                                    val intent = WalletActivity.getIntent(this, store)

                                    startActivity(intent)
                                },
                                content = {

                                    Text(store)
                                    Text(tickets)
                                })
                        })
                    }
                },
                button = {
                    FloatingActionButton(
                        onClick = {
                            val intent = StoresActivity.getIntent(this)

                            startActivity(intent)
                        },
                        shape = RoundedCornerShape(16.dp),
                    ){
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Search stations",
                            tint = Color.White,
                        )
                    }
                })
        }
    }
}