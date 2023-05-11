package com.example.avalanche.core

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.avalanche.ui.drm.WriterActivity
import com.example.core.environment.Constants.Companion.SELECT_APDU


class DrmHostApduWriter : HostApduService() {

    companion object {

        private const val TAG = "DrmHostApduWriter"

        private val A_OKAY = byteArrayOf(
            0x90.toByte(), // SW1	Status byte 1 - Command processing status
            0x00.toByte()  // SW2	Status byte 2 - Command processing qualifier
        )
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

        Log.i(TAG, "processCommandApdu()")

        //
        // First command: NDEF Tag Application select (Section 5.5.2 in NFC Forum spec)
        //
        if (SELECT_APDU.contentEquals(commandApdu)
        ) {
            Log.i(TAG, "processCommandApdu() | SELECT_APDU triggered")

            return A_OKAY
        }

        val challengeId = commandApdu.decodeToString().dropLast(2)

        Log.i(TAG, "processCommandApdu() | CHALLENGE_ID triggered $challengeId")

        val intent = Intent(this, WriterActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra(WriterActivity.ChallengeIdKey, challengeId)

        startActivity(intent)

        return A_OKAY
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated() | Fired! Reason: $reason")
    }
}