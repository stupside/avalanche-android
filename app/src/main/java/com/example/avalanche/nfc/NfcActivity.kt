package com.example.avalanche.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold


class NfcActivity : ComponentActivity() {

    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val pendingIntent: PendingIntent by lazy {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AvalancheScaffold(activity = this, button = {
            }) {
                Text("NfcActivity")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)

        filter.addDataType("text/plain")

        nfcAdapter.enableForegroundDispatch(
            this,
            pendingIntent,
            arrayOf(filter),
            arrayOf(arrayOf(NfcA::class.java.name))
        )
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val messages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES,
                    NdefMessage::class.java
                )
            } else {
                throw NotImplementedError()
            }

            Toast.makeText(this, messages.toString(), Toast.LENGTH_LONG).show()

        }
    }
}