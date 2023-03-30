package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            AvalancheScaffold(activity = this, content = {

                val storeState: StoreService.GetStoreProto.Response? by storeVm.store.observeAsState(null)

                storeState?.let { store ->
                    AvalancheSection(title = store.name) {

                        val tickets: List<TicketService.GetTicketsProto.Response> by walletVm.tickets.observeAsState(
                            emptyList()
                        )

                        AvalancheList(elements = tickets, template = { ticket ->

                            AvalancheListElement(
                                onClick = {
                                    // TODO: load ticket activity
                                },
                                content = {
                                    Text(ticket.name)
                                })
                        })
                    }

                }

            }, button = {
                FloatingActionButton(
                    onClick = {
                        val intent = StoreActivity.getIntent(this, StoreIdKey)

                        startActivity(intent)
                    },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add FAB",
                        tint = Color.White,
                    )
                }
            })
        }
    }
}

