package com.example.avalanche.views.payment

import Avalanche.Passport.TicketService
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.WalletActivity
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import java.util.concurrent.TimeUnit

@Composable
fun PaymentView(context: Context, viewModel: PaymentViewModel, storeId: String, planId: String) {

    LaunchedEffect(storeId) {
        try {
            viewModel.loadTickets(context, storeId)
        } catch (_: Exception) {
        }
    }

    var ticketName by rememberSaveable { mutableStateOf("") }
    var days by rememberSaveable { mutableStateOf(0) }

    val tickets: List<TicketService.GetTicketsProto.Response>? by viewModel.tickets.collectAsState()

    Scaffold(topBar = {
        PaymentTopBar(context)
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                PaymentTicketNameInput(tickets) { input ->
                    ticketName = input
                }

                PaymentTicketDateInput { input ->
                    days = input
                }

                PaymentTicketConfirmationButton(ticketName, days) {

                    val amount = 10000 // TODO: view fake the payment

                    val intent =
                        WalletActivity.getIntent(context, storeId)

                    val ticket = tickets?.firstOrNull { ticket ->
                        ticket.name == ticketName
                    }

                    if (ticket == null) {
                        try {
                            viewModel.createTicket(
                                context,
                                storeId,
                                ticketName
                            ) { ticketId ->
                                viewModel.purchase(
                                    context,
                                    ticketId,
                                    planId,
                                    days
                                ) { orderId ->
                                    try {
                                        viewModel.receive(context, orderId, amount) {
                                            context.startActivity(intent)
                                        }
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                        } catch (_: Exception) {
                        }
                    } else {
                        val ticketId = ticket.ticketId

                        try {
                            viewModel.purchase(context, ticketId, planId, days) { orderId ->
                                viewModel.receive(context, orderId, amount) {
                                    context.startActivity(intent)
                                }
                            }
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentTopBar(context: Context) {
    TopAppBar(title = {
        Text("Payment")
    }, navigationIcon = {
        AvalancheGoBackButton(context)
    })
}

@Composable
fun PaymentTicketNameInput(
    tickets: List<TicketService.GetTicketsProto.Response>?,
    onTicketNameChange: (ticketName: String) -> Unit
) {
    var ticketName by rememberSaveable { mutableStateOf("") }

    var search by rememberSaveable { mutableStateOf(false) }

    Row {
        OutlinedTextField(
            value = ticketName,
            placeholder = {
                Text("Ticket name")
            },
            onValueChange = {
                ticketName = it

                onTicketNameChange(ticketName)
            })

        IconButton(onClick = { search = true }) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Reference an existing ticket"
            )
        }
    }

    if (search) {
        PaymentTicketExplorer({ search = false }) {
            Text(
                "Select the desired ticket",
                style = MaterialTheme.typography.titleLarge
            )

            tickets?.let {
                AvalancheList(tickets) {
                    PaymentTicketExplorerContentItem(it.name, ticketName == it.name) {
                        ticketName = it.name

                        onTicketNameChange(ticketName)

                        search = false
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentTicketExplorer(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun PaymentTicketExplorerContentItem(ticketName: String, isSelected: Boolean, onClick: () -> Unit) {

    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(ticketName) },
        tonalElevation = if (isSelected) 3.dp else 0.dp
    )
}

@Composable
fun PaymentTicketDateInput(onDateChange: (inDays: Int) -> Unit) {

    val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    val datePickerState =
        rememberDatePickerState(
            initialDisplayedMonthMillis = now.timeInMillis,
            initialDisplayMode = DisplayMode.Picker,
        )

    val availableInDays = TimeUnit.MILLISECONDS.toDays(
        datePickerState.selectedDateMillis?.minus(now.timeInMillis)
            ?: 0
    ).toInt()

    var pick by rememberSaveable { mutableStateOf(false) }

    Button(onClick = {
        pick = true
    }) {
        Text("Select a date")
    }

    if (pick) {
        DatePickerDialog(
            onDismissRequest = {
                pick = false
            },
            dismissButton = {
                Button(onClick = {
                    pick = false
                }) {
                    Text("Dismiss")
                }
            },
            confirmButton = {
                Button(onClick = {

                    pick = false

                    onDateChange(availableInDays)
                }) {
                    Text("Confirm")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = { utcDateInMills ->
                    utcDateInMills >= now.timeInMillis
                },
            )
        }
    }
}

@Composable
fun PaymentTicketConfirmationButton(
    ticketName: String,
    inDays: Int,
    onConfirm: () -> Unit
) {

    var confirm by remember { mutableStateOf(false) }

    Button(
        enabled = ticketName.isNotEmpty() && ticketName.length >= 5,
        onClick = {
            confirm = true
        }) {
        Text("Confirm payment")
    }

    if (confirm) {
        AlertDialog(
            onDismissRequest = {
                confirm = false
            },
            title = {
                Text("Payment confirmation")
            },
            text = {
                Text("Your ticket will be available in $inDays days")
            },
            confirmButton = {
                Button(onClick = {
                    confirm = false

                    onConfirm()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = {
                    confirm = false
                }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

