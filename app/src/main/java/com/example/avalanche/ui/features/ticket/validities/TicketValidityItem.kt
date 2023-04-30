package com.example.avalanche.ui.features.ticket.validities

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import avalanche.vault.ticket.Ticket.GetOneTicketRpc.Response.ValidityKind
import java.util.concurrent.TimeUnit


@Composable
fun TicketValidityItem(
    from: Long,
    to: Long,
    span: Long,
    kind: ValidityKind
) {

    fun getDateTime(seconds: Long): String {
        val days = TimeUnit.SECONDS.toDays(seconds)

        if (days == 0L) {
            val hours = TimeUnit.SECONDS.toHours(seconds)

            if (hours == 0L) {
                return "${TimeUnit.SECONDS.toMinutes(seconds)} minutes"
            }

            return "$hours hours"
        } else {
            return "$days days"
        }
    }



    ListItem(
        headlineContent = {
            Text("${getDateTime(to - from)} validity")
        },
        trailingContent = {

            when (kind) {
                ValidityKind.EARLY -> {
                    Text("Valid in ${getDateTime(span)}")
                }

                ValidityKind.VALID -> {
                    Text("Valid for ${getDateTime(span)}")
                }

                ValidityKind.LATE -> {
                    Text("Valid ${getDateTime(span)} ago")
                }

                else -> {}
            }
        }
    )
}
