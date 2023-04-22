package com.example.avalanche.ui.features.ticket.validities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import avalanche.vault.ticket.Ticket
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TicketValidityItem(from: Long, to: Long, kind: Ticket.GetOneTicketRpc.Response.ValidityKind) {

    fun getDateTime(seconds: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(seconds * 1000L)
    }

    ListItem(
        headlineContent = {
            Text("${getDateTime(from)} - ${getDateTime(to)}")
        },
        trailingContent = {

            when (kind) {
                Ticket.GetOneTicketRpc.Response.ValidityKind.EARLY ->
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )

                Ticket.GetOneTicketRpc.Response.ValidityKind.VALID ->
                    Icon(
                        Icons.Default.Done,
                        contentDescription = null
                    )

                Ticket.GetOneTicketRpc.Response.ValidityKind.LATE ->
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )

                else -> {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        })
}
