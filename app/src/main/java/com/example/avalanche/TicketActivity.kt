package com.example.avalanche

import Avalanche.Passport.TicketService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.TicketViewModel

class TicketActivity : ComponentActivity() {

    companion object {
        private const val TicketIdKey = "TicketId"

        fun getIntent(context: Context, ticketId: String): Intent {
            return Intent(context, TicketActivity::class.java).putExtra(TicketIdKey, ticketId)
        }
    }

    private lateinit var ticketVm: TicketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ticketId = intent.getStringExtra(TicketIdKey)!!

        ticketVm = TicketViewModel(ticketId)

        ticketVm.loadTicket(this)

        setContent {

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Payments")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        val ticketState: TicketService.GetTicketProto.Response? by ticketVm.ticket.observeAsState()

                        ticketState?.let { ticket ->
                            TicketHeader(context = this@TicketActivity, name = ticket.name)
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun TicketHeader(context: Context, name: String) {
    ElevatedCard {
        Box(Modifier.fillMaxWidth()) {
            Text(name)
        }
    }
}