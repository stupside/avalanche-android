package com.example.avalanche.views.ticket

import Avalanche.Passport.TicketService
import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.AvalancheHeader
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.md_theme_light_error
import com.example.avalanche.core.ui.theme.md_theme_light_surfaceTint
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketView(
    context: Context,
    viewModel: TicketViewModel,
    ticketId: String,
    deviceIdentifier: String
) {

    LaunchedEffect(ticketId) {
        try {
            viewModel.loadTicket(context, ticketId)
        } catch (_: Exception) {
        }
    }

    val ticket: TicketService.GetTicketProto.Response? by viewModel.ticket.observeAsState()

    Scaffold(topBar = {
        TicketTopBar(context)
    }, content = { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).background(color = md_theme_light_surfaceTint), verticalArrangement = Arrangement.Top) {

            ticket?.let { ticket ->

                TicketHeader(ticketName = ticket.name)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TicketSealAction(
                        isSealed = ticket.isSealed,
                        onSeal = {
                            try {
                                viewModel.sealTicket(context, ticketId, deviceIdentifier)
                            } catch (_: Exception) {
                            }
                        },
                        onUnseal = {
                            try {
                                viewModel.unsealTicket(context, ticketId, deviceIdentifier)
                            } catch (_: Exception) {
                            }
                        },
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Ticket validity",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    AvalancheList(elements = ticket.validitiesList, template = { validity ->
                        TicketValidityItem(
                            validity.from.seconds,
                            validity.to.seconds,
                            validity.isNow
                        )
                    })
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
fun TicketValidityItem(from: Long, to: Long, isNow: Boolean) {
    val durationStr = getDuration(to - from)
    val days = (to - from) / 86400
    val fromStr = getDateTime(from)
    val toStr = getDateTime(to)
    ListItem(
        headlineContent = {
            Text("From: $fromStr")
            Text("To: $toStr")
        }, trailingContent = {
            if (days < 1) {
                Text("Duration: $durationStr")
            } else {
                Text("Duration: $durationStr Days")
            }
            //TODO $isNow: replace Icons
            //Text("In Bracket: $isNow")
        })
}

private fun getDateTime(seconds: Long): String {

    val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)

    return simpleDateFormat.format(seconds * 1000L)
}

private fun getDuration(seconds: Long): String {
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

@Composable
fun TicketSealAction(
    isSealed: Boolean,
    onSeal: () -> Unit,
    onUnseal: () -> Unit
) {

    var checked by remember { mutableStateOf(isSealed) }

    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (checked) {
            Text("Sealed", style = MaterialTheme.typography.titleMedium)
        } else {
            Text("Unsealed", style = MaterialTheme.typography.titleMedium)
        }

        Switch(
            modifier = Modifier.padding(16.dp),
            checked = checked,
            onCheckedChange = {
                checked = it

                if (checked) {
                    onSeal()
                } else {
                    onUnseal()
                }
            })
    }
}
