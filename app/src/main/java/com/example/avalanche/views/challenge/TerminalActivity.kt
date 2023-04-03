package com.example.avalanche.views.challenge

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.ui.theme.AvalancheTheme


abstract class TerminalActivity : ComponentActivity() {

    private val adapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val pending: PendingIntent by lazy {

        val intent = Intent(this, javaClass)

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        PendingIntent.getActivity(
            this,
            0,
            intent,
            flag
        )
    }

    private val terminalVm: TerminalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: this should be loaded NFC / QR see HostApdu

        setContent {
            AvalancheTheme {
                TerminalView(context = this, viewModel = terminalVm)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)

        filter.addDataType("text/plain")

        adapter.enableForegroundDispatch(
            this,
            pending,
            arrayOf(filter),
            arrayOf(arrayOf(NfcA::class.java.name))
        )
    }

    override fun onPause() {
        super.onPause()

        adapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES,
                    NdefMessage::class.java
                )
            } else {
                throw NotImplementedError()
            }
        }
    }
}