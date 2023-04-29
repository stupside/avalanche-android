package com.example.avalanche.core

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.core.environment.Constants.Companion.SELECT_APDU

class DrmHostApduService : HostApduService() {

    companion object {

        private const val TAG = "DrmHostApduService"

        private val A_OKAY = byteArrayOf(
            0x90.toByte(), // SW1	Status byte 1 - Command processing status
            0x00.toByte()  // SW2	Status byte 2 - Command processing qualifier
        )

        private val A_ERROR = byteArrayOf(
            0x6A.toByte(), // SW1	Status byte 1 - Command processing status
            0x82.toByte()   // SW2	Status byte 2 - Command processing qualifier
        )
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

        Log.i(TAG, "processCommandApdu()")

        //
        // First command: NDEF Tag Application select (Section 5.5.2 in NFC Forum spec)
        //
        if (SELECT_APDU.contentEquals(commandApdu)
        ) {
            Log.i(TAG, "APDU_SELECT triggered.");

            return A_OKAY
        }

        Log.wtf(TAG, "processCommandApdu() | unhandled command ${commandApdu.decodeToString()}")

        return A_ERROR
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated() Fired! Reason: $reason")
    }
}