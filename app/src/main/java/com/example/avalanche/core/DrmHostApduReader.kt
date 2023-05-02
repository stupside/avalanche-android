package com.example.avalanche.core

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import androidx.annotation.AnyThread
import com.example.core.environment.Constants

class DrmHostApduReader constructor(
    private val storeId: String,
    private val prepareChallenge: (storeId: String) -> String,
    private val watchChallenge: (challengeId: String) -> Unit
) : NfcAdapter.ReaderCallback {

    @AnyThread
    override fun onTagDiscovered(tag: Tag?) {

        val iso = IsoDep.get(tag)

        iso.connect()

        iso.transceive(Constants.SELECT_APDU)

        Log.i("AvalancheReaderCallback", "onTagDiscovered() | prepareChallenge")

        val challengeId = prepareChallenge(storeId)

        Log.i("AvalancheReaderCallback", "onTagDiscovered() | watchChallenge")

        watchChallenge(challengeId)

        Log.i("AvalancheReaderCallback", "onTagDiscovered() | transceive $challengeId")

        iso.transceive(challengeId.encodeToByteArray() + byteArrayOf(0x90.toByte(), 0x00))

        iso.close()
    }
}