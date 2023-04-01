package com.example.avalanche

import Avalanche.Passport.TicketService
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.AvalancheHeader
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.TicketViewModel
import android.provider.Settings
import androidx.compose.material3.*
import com.example.avalanche.core.ui.shared.list.AvalancheList


class TicketActivity : ComponentActivity() {

    companion object {
        private const val TicketIdKey = "TicketId"

        fun getIntent(context: Context, ticketId: String): Intent {
            return Intent(context, TicketActivity::class.java).putExtra(TicketIdKey, ticketId)
        }
    }

    private lateinit var ticketVm: TicketViewModel

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ticketId = intent.getStringExtra(TicketIdKey)!!

        ticketVm = TicketViewModel(ticketId)

        ticketVm.loadTicket(this)

        setContent {

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Ticket")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        val ticketState: TicketService.GetTicketProto.Response? by ticketVm.ticket.observeAsState()

                        ticketState?.let { ticket ->
                            TicketHeader(ticketName = ticket.name)
                            Row(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                var preset: Boolean = false
                                if (ticket.isSealed)
                                    preset = true
                                var checked by remember { mutableStateOf(preset) }
                                val deviceId: String = Settings.Secure.getString(
                                    contentResolver,
                                    Settings.Secure.ANDROID_ID
                                )
                                if (checked) {
                                    Text(text = "Sealed")
                                    if (!ticket.isSealed)
                                        ticketVm.sealTicket(this@TicketActivity, ticketId, deviceId)
                                } else {
                                    Text(text = "Unsealed")
                                    if (ticket.isSealed)
                                        ticketVm.unsealTicket(
                                            this@TicketActivity,
                                            ticketId,
                                            deviceId
                                        )
                                }
                                Switch(
                                    modifier = Modifier.semantics {
                                        contentDescription = "Seal or Unseal the Ticket"
                                    },
                                    checked = checked,
                                    onCheckedChange = { checked = it })
                            }

                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth()
                            ) {
                                Text("Times when Valid")

                                val validityList = ticket.validitiesList

                                AvalancheList(elements = validityList, template = { validity ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(text = "From: " + validity.from.toString())
                                        Text(text = "To: " + validity.to.toString())
                                    }
                                })
                            }
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun TicketHeader(ticketName: String) {
    AvalancheHeader(ticketName, null, null)
}