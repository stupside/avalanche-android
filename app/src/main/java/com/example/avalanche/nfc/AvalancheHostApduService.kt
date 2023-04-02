package com.example.avalanche.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class AvalancheHostApduService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.i(TAG, "processCommandApdu: $commandApdu")

        return commandApdu
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated: $reason")
    }

    companion object {
        const val TAG = "APDU"
    }
}