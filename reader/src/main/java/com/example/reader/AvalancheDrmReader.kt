package com.example.reader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.reader.ui.theme.AvalancheTheme

class AvalancheDrmReaderReaderCallback constructor(private val challengeId: String) :
    NfcAdapter.ReaderCallback {

    companion object {

        val SELECT_APDU = byteArrayOf(
            0x00.toByte(),
            0xA4.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            0x07.toByte(),

            0xF0.toByte(), // AID apduservice.xml
            0x39.toByte(),
            0xE2.toByte(),
            0xD3.toByte(),
            0xC4.toByte(),
            0xB5.toByte(),
            0x00.toByte(),

            0x00.toByte()
        )
    }

    override fun onTagDiscovered(tag: Tag?) {

        val iso = IsoDep.get(tag)

        iso.connect()

        iso.transceive(SELECT_APDU)

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