package com.example.avalanche.core

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class DrmHostApduService : HostApduService() {

    companion object {

        const val TAG = "APDU"

        private val EXCHANGE_CHALLENGE_ID: Byte = 0x01

        private val SELECT_AID_COMMAND = byteArrayOf(
            0x00.toByte(), // Class
            0xA4.toByte(), // Instruction
            0x04.toByte(), // Parameter 1
            0x00.toByte(), // Parameter 2
            0x06.toByte(), // Length
            0xF0.toByte(), // AID
            0xAB.toByte(),
            0xCD.toByte(),
            0xEF.toByte(),
            0x00.toByte(),
            0x00.toByte()
        )

        // OK status sent in response to SELECT AID command (0x9000)
        private val SELECT_RESPONSE_OK = byteArrayOf(0x90.toByte(), 0x00.toByte())
    }

    // https://github.com/classycodeoss/android-nfc-http-tunnel/blob/master/android/NFCHTTPTunnel/app/src/main/java/com/classycode/nfchttptun/TunnelApduService.java
    // https://blog.classycode.com/tunneling-http-over-nfc-on-android-using-host-card-emulation-907947a721ac
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

        if(SELECT_AID_COMMAND.contentEquals(commandApdu)){
            return SELECT_RESPONSE_OK
        }
        else if(commandApdu[0] == EXCHANGE_CHALLENGE_ID) {
            
        }

        return commandApdu
    }

    override fun onDeactivated(reason: Int) {

        Log.i(TAG, "onDeactivated: $reason")
    }
}