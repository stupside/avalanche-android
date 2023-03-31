package com.example.avalanche

import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.avalanche.core.ui.shared.AvalancheBottomBar
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.PurchaseViewModel
import com.example.avalanche.viewmodels.WalletViewModel
import java.util.concurrent.TimeUnit

class PaymentCheckInActivity : ComponentActivity() {

    companion object {
        private const val PlanIdKey = "PlanId"
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String, planId: String): Intent {
            return Intent(context, PaymentCheckInActivity::class.java)
                .putExtra(StoreIdKey, storeId)
                .putExtra(PlanIdKey, planId)
        }
    }

    private lateinit var purchaseVm: PurchaseViewModel
    private lateinit var walletVm: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!
        val planId = intent.getStringExtra(PlanIdKey)!!

        walletVm = WalletViewModel(storeId)
        purchaseVm = PurchaseViewModel()

        val calendar = Calendar.getInstance()

        val now = calendar.timeInMillis

        setContent {

            var ticketId by remember { mutableStateOf("") }

            val inputDate = remember { mutableStateOf(now) }

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Check in")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->

                    Column(modifier = Modifier.padding(paddingValues)) {

                        Column {

                            val ticketStates: List<TicketService.GetTicketsProto.Response>? by walletVm.tickets.collectAsState()

                            ticketStates?.let { tickets ->
                                AvalancheList(elements = tickets) { ticket ->
                                    ListItem(
                                        modifier = Modifier.clickable(onClick = {
                                            ticketId = ticket.ticketId
                                        }),
                                        headlineContent = { Text(ticket.name) },
                                        trailingContent = {
                                            Checkbox(
                                                checked = ticket.name == ticketId,
                                                onCheckedChange = null
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        AndroidView(
                            { CalendarView(it) },
                            modifier = Modifier.wrapContentSize(),
                            update = { views ->
                                views.setOnDateChangeListener { picker, _, _, _ ->
                                    inputDate.value = picker.date
                                }
                            }
                        )

                        val availableInDays = TimeUnit.MILLISECONDS.toDays(inputDate.value - now)

                        Button(onClick = {
                            purchaseVm.purchase(this@PaymentCheckInActivity, ticketId, planId, availableInDays.toInt())
                        }) {
                            Text("Buy")

                            Text("Ticket will be available in $availableInDays days")
                        }
                    }
                }, bottomBar = {
                    AvalancheBottomBar(this, floating = null)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        walletVm.loadWallet(this)
    }
}