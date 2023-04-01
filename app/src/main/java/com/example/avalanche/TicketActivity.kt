package com.example.avalanche

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Secure.getString
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.nfc.NfcActivity
import com.example.avalanche.views.ticket.TicketView
import com.example.avalanche.views.ticket.TicketViewModel


class TicketActivity : NfcActivity() {

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

        val ticketId = intent.getStringExtra(TicketIdKey)

        ticketId?.let {
            val deviceIdentifier = getString(contentResolver, Settings.Secure.ANDROID_ID)

            setContent {
                AvalancheTheme {
                    TicketView(context = this, viewModel = ticketVm, ticketId = ticketId, deviceIdentifier = deviceIdentifier)
                }
            }
        }
    }
}