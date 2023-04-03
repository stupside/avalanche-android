package com.example.avalanche.views.ticket

import Avalanche.Passport.TicketService
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.AvalancheHeader
import com.example.avalanche.core.ui.shared.list.AvalancheList
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketView(
    context: Context,
    viewModel: TicketViewModel,
    ticketId: String,
    deviceIdentifier: String
) {

    LaunchedEffect(ticketId, deviceIdentifier) {
        try {
            viewModel.loadTicket(context, ticketId, deviceIdentifier)
        } catch (_: Exception) {
        }
    }

    val ticket: TicketService.GetTicketProto.Response? by viewModel.ticket.observeAsState()
    val seal: Boolean? by viewModel.seal.observeAsState()

    Scaffold(topBar = {
        TicketTopBar(context)
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ticket?.let { ticket ->

                    TicketHeader(ticketName = ticket.name)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TicketSealAction(
                            isSealed = ticket.isSealed,
                            onSeal = {
                                try {
                                    viewModel.sealTicket(context, ticket.storeId, ticketId, deviceIdentifier)
                                } catch (_: Exception) {
                                }
                            },
                            onUnseal = if (ticket.isSealed && seal == true) {
                                {
                                    try {
                                        viewModel.unsealTicket(context, ticketId, deviceIdentifier)
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                            else null,
                        )
                    }

                    var duration by remember {
                        mutableStateOf<Long?>(null)
                    }

                    Column {
                        Text(
                            "Ticket validity",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )

                        AvalancheList(elements = ticket.validitiesList, template = { validity ->
                            Surface(onClick = {
                                duration = validity.to.seconds - validity.from.seconds
                            }) {
                                TicketValidityItem(
                                    validity.from.seconds,
                                    validity.to.seconds,
                                    validity.isNow
                                )
                            }
                        })
                    }

                    duration?.let {
                        AlertDialog({ duration = null }) {
                            Surface(
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
                                            "Validity details",
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        IconButton(onClick = {
                                            duration = null
                                        }) {
                                            Icon(Icons.Default.Close, contentDescription = null)
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        val days = msToDays(it)

                                        if (days.contains(":")) {
                                            Text(text = "Duration: $duration")
                                        } else {
                                            Text(text = "Duration: $duration Day(s)")
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    })
}

@Composable
fun TicketTopBar(context: Context) {
    TopAppBar(title = {
        Text("Ticket")
    }, navigationIcon = {
        AvalancheGoBackButton(context)
    })
}

@Composable
fun TicketHeader(ticketName: String) {
    AvalancheHeader(ticketName, null, null)
}

@Composable
fun TicketSealAction(
    isSealed: Boolean,
    onSeal: () -> Unit,
    onUnseal: (() -> Unit)?
) {

    var checked by remember { mutableStateOf(isSealed) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(if (checked) "Sealed" else "Unsealed", style = MaterialTheme.typography.bodyMedium)

        Switch(
            checked = checked,
            enabled = (isSealed && onUnseal != null) || !isSealed,
            onCheckedChange = {
                checked = it

                if (checked) {
                    onSeal()
                } else {
                    onUnseal?.invoke()
                }
            })
    }
}

@Composable
fun TicketValidityItem(from: Long, to: Long, isNow: Boolean) {

    val fromStr = getDateTime(from)
    val toStr = getDateTime(to)

    ListItem(
        headlineContent = {
            Text("$fromStr - $toStr")
        },
        trailingContent = {
            if (isNow) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = null
                )
            }
        })
}

private fun getDateTime(seconds: Long): String {

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    return simpleDateFormat.format(seconds * 1000L)
}

private fun msToDays(seconds: Long): String {
    return try {
        val days = seconds / 86400
        if (days >= 1) {
            days.toString()
        } else {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            simpleDateFormat.format(seconds * 1000L)
        }
    } catch (_: Exception) {
        "ValueError"
    }
}

