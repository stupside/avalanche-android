package com.example.avalanche.ui.drm

import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avalanche.core.DrmHostApduReader
import com.example.avalanche.ui.theme.AvalancheTheme
import org.koin.androidx.compose.koinViewModel

class ReaderActivity : ComponentActivity() {

    companion object {
        const val INTENT_EXTRA_STORE_ID = "storeId"
    }

    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val storeId = intent.getStringExtra(INTENT_EXTRA_STORE_ID)!!

        setContent {
            
            AvalancheTheme {

                ReaderView(
                    koinViewModel(),
                    { finish() },
                    nfcAdapter.isEnabled
                ) { prepareChallenge, watchChallenge ->

                    Log.i("ReaderActivity", "setupTag() | $storeId")

                    nfcAdapter.enableReaderMode(
                        this,
                        DrmHostApduReader(storeId, prepareChallenge, watchChallenge),
                        NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                        null
                    )
                }
            }
        }
    }

    private fun disableReader() {
        nfcAdapter.disableReaderMode(this)
    }

    override fun onPause() {
        super.onPause()

        disableReader()
    }
}