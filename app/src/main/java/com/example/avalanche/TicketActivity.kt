package com.example.avalanche

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.views.ticket.TicketView
import com.example.avalanche.views.ticket.TicketViewModel


class TicketActivity : ComponentActivity() {

    companion object {
        private const val TicketIdKey = "TicketId"

        fun getIntent(context: Context, ticketId: String): Intent {
            return Intent(context, TicketActivity::class.java).putExtra(TicketIdKey, ticketId)
        }
    }

    private val ticketVm: TicketViewModel by viewModels()

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ticketId = intent.getStringExtra(TicketIdKey)!!

        val deviceIdentifier = Constants.DEVICE_IDENTIFIER

        setContent {
            AvalancheTheme {
                TicketView(
                    context = this,
                    viewModel = ticketVm,
                    ticketId = ticketId,
                    deviceIdentifier = deviceIdentifier
                )
            }
        }
    }
}