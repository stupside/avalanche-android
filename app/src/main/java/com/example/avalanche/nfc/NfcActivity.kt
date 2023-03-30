package com.example.avalanche.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity


abstract class NfcActivity : ComponentActivity() {

    private val adapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val pending: PendingIntent by lazy {

        val intent = Intent(this, javaClass)

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.FLAG_MUTABLE
        }
        else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        PendingIntent.getActivity(
            this,
            0,
            intent,
            flag
        )
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

        Toast.makeText(this, "Nfc", Toast.LENGTH_LONG).show()

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