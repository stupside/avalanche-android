package com.example.avalanche.ui

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avalanche.core.DrmHostApduService

class AvalancheDrmReaderReaderCallback : NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag?) {
        val iso = IsoDep.get(tag)

        iso.connect()

        iso.transceive(DrmHostApduService.SELECT_APDU)

        iso.close()
    }
}

class AvalancheDrmReader : ComponentActivity() {
    private lateinit var nfc: NfcAdapter

    private lateinit var callback: AvalancheDrmReaderReaderCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfc = NfcAdapter.getDefaultAdapter(this)

        callback = AvalancheDrmReaderReaderCallback()

        setContent {

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