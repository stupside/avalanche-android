package com.example.avalanche.ui.features.ticket.validities

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import avalanche.vault.ticket.Ticket
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun TicketValidityItem(
    from: Long,
    to: Long,
    kind: Ticket.GetOneTicketRpc.Response.ValidityKind,
    span: Long
) {

    fun getDateTime(seconds: Long): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(
            TimeUnit.SECONDS.toMillis(
                seconds
            )
        )
    }

    val days = TimeUnit.SECONDS.toDays(span)

    val availability = if (days == 0L) {

        TimeUnit.SECONDS.toMinutes(span)
    } else {

        days
    }

    ListItem(
        headlineContent = {

            Row {
                Text(getDateTime(from))
                Spacer(modifier = Modifier.padding(ButtonDefaults.IconSpacing))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(ButtonDefaults.IconSpacing))
                Text(getDateTime(to))
            }
        },
        trailingContent = {

            BadgedBox(badge = {
                Badge {
                    Text(availability.toString())
                }
            }) {
                when (kind) {
                    Ticket.GetOneTicketRpc.Response.ValidityKind.EARLY ->
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null
                        )

                    Ticket.GetOneTicketRpc.Response.ValidityKind.VALID ->
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )

                    Ticket.GetOneTicketRpc.Response.ValidityKind.LATE ->
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )

                    else -> {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }


        })
}