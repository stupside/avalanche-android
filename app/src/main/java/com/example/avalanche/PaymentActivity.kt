package com.example.avalanche

import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.PurchaseViewModel
import com.example.avalanche.viewmodels.WalletViewModel
import java.util.concurrent.TimeUnit


class PaymentActivity : ComponentActivity() {

    companion object {
        private const val PlanIdKey = "PlanId"
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String, planId: String): Intent {
            return Intent(context, PaymentActivity::class.java)
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

        setContent {

            val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Payment")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }) { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            val ticketStates: List<TicketService.GetTicketsProto.Response>? by walletVm.tickets.collectAsState()

                            var ticketName by rememberSaveable { mutableStateOf("") }

                            var selecting by rememberSaveable { mutableStateOf(false) }

                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = ticketName,
                                placeholder = {
                                    Text("Enter a ticket name")
                                },
                                onValueChange = { ticketName = it },
                                trailingIcon = {
                                    ticketStates?.let {
                                        IconButton(onClick = { selecting = true }) {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription = "Select an existing ticket"
                                            )
                                        }
                                    }
                                })

                            if (selecting) {
                                AlertDialog(onDismissRequest = { selecting = false }) {
                                    Surface(
                                        modifier = Modifier
                                            .wrapContentSize(),
                                        shape = MaterialTheme.shapes.large
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                "Select the desired ticket",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                            ticketStates?.let { tickets ->
                                                AvalancheList(elements = tickets) { ticket ->
                                                    ListItem(
                                                        modifier = Modifier.clickable(onClick = {
                                                            selecting = false
                                                            ticketName = ticket.name
                                                        }),
                                                        headlineContent = { Text(ticket.name) },
                                                        tonalElevation = if (ticket.name == ticketName) 3.dp else 0.dp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            val datePickerState =
                                rememberDatePickerState(
                                    initialDisplayedMonthMillis = now.timeInMillis,
                                    initialDisplayMode = DisplayMode.Picker
                                )

                            ElevatedCard {
                                DatePicker(
                                    state = datePickerState,
                                    dateValidator = { utcDateInMills ->
                                        utcDateInMills >= now.timeInMillis
                                    }
                                )
                            }

                            Row(horizontalArrangement = Arrangement.End) {
                                ticketStates?.let {
                                    var openDialog by remember { mutableStateOf(false) }

                                    Button(
                                        enabled = ticketName.isNotEmpty() && ticketName.length >= 5,
                                        onClick = {
                                            openDialog = true
                                        }) {
                                        Text("Pay now")
                                    }

                                    if (openDialog) {

                                        val availableInDays = TimeUnit.MILLISECONDS.toDays(
                                            datePickerState.selectedDateMillis?.minus(now.timeInMillis)
                                                ?: 0
                                        ).toInt()

                                        val ticket =
                                            ticketStates?.firstOrNull { it.name == ticketName }

                                        AlertDialog(
                                            onDismissRequest = {
                                                openDialog = false
                                            },
                                            title = {
                                                Text("Payment confirmation")
                                            },
                                            text = {
                                                Text("Your ticket will be available in $availableInDays days")
                                            },
                                            confirmButton = {
                                                TextButton(
                                                    onClick = {
                                                        if (ticket == null) {
                                                            walletVm.createTicket(
                                                                this@PaymentActivity,
                                                                ticketName,
                                                                onCreated = { ticketId ->
                                                                    purchaseVm.purchase(
                                                                        this@PaymentActivity,
                                                                        ticketId,
                                                                        planId,
                                                                        availableInDays,
                                                                        onPurchase = { orderId ->
                                                                            purchaseVm.receive(
                                                                                this@PaymentActivity,
                                                                                orderId,
                                                                                100000,
                                                                                onReceived = {
                                                                                    val intent =
                                                                                        WalletActivity.getIntent(
                                                                                            this@PaymentActivity,
                                                                                            storeId
                                                                                        )

                                                                                    startActivity(
                                                                                        intent
                                                                                    )
                                                                                }
                                                                            )
                                                                        }
                                                                    )
                                                                })
                                                        } else {
                                                            purchaseVm.purchase(
                                                                this@PaymentActivity,
                                                                ticket.ticketId,
                                                                planId,
                                                                availableInDays,
                                                                onPurchase = { orderId ->
                                                                    purchaseVm.receive(
                                                                        this@PaymentActivity,
                                                                        orderId,
                                                                        100000,
                                                                        onReceived = {
                                                                            val intent =
                                                                                WalletActivity.getIntent(
                                                                                    this@PaymentActivity,
                                                                                    storeId
                                                                                )

                                                                            startActivity(intent)
                                                                        }
                                                                    )
                                                                }
                                                            )
                                                        }
                                                    }
                                                ) {
                                                    Text("Pay")
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(
                                                    onClick = {
                                                        openDialog = false
                                                    }
                                                ) {
                                                    Text("Close")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        walletVm.loadWallet(this)
    }
}