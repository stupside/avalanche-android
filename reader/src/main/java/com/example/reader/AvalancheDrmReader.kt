package com.example.reader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.core.environment.Constants
import com.example.reader.ui.theme.AvalancheTheme

class AvalancheDrmReaderReaderCallback constructor(private val challengeId: String) :
    NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag?) {

        val iso = IsoDep.get(tag)

        iso.connect()

        iso.transceive(Constants.SELECT_APDU)

        iso.transceive(challengeId.encodeToByteArray())

        iso.close()
    }
}

class AvalancheDrmReader : ComponentActivity() {
    private lateinit var nfc: NfcAdapter

    private lateinit var callback: AvalancheDrmReaderReaderCallback

    companion object {
        private const val StoreIdKey = "drm.storeId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfc = NfcAdapter.getDefaultAdapter(this)

        val storeId = intent.getStringExtra(StoreIdKey)!!

        callback = AvalancheDrmReaderReaderCallback(storeId)

        setContent {

            AvalancheTheme {
                Text("AvalancheDrmReader")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        nfc.enableReaderMode(
            this,
            callback,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    override fun onPause() {
        super.onPause()

        nfc.disableReaderMode(this)
    }
}