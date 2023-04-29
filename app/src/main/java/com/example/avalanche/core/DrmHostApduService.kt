package com.example.avalanche.core

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import avalanche.drm.auth.AcquireChallengeRpcKt
import avalanche.drm.auth.AuthServiceGrpcKt
import com.example.core.di.services.AvalancheIdentityService
import com.example.core.environment.Constants.Companion.SELECT_APDU
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class DrmHostApduService : HostApduService() {

    private val identity: AvalancheIdentityService by inject()

    private val channel: ManagedChannel by inject()
    private val credentials: BearerTokenCallCredentials by inject()

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
            Log.i(TAG, "processCommandApdu() | SELECT_APDU triggered.")

            return A_OKAY
        }

        val body = commandApdu.decodeToString()

        val service =
            AuthServiceGrpcKt.AuthServiceCoroutineStub(channel).withCallCredentials(credentials)

        val request = AcquireChallengeRpcKt.command {
            this.challengeId = ""
        }

        val flow = service.acquire(request)

        runBlocking {

            flow.collect {

                Log.wtf(TAG, "processCommandApdu() | ${it.message} ${it.success}")
            }
        }

        Log.wtf(TAG, "processCommandApdu() | unhandled command ${commandApdu.decodeToString()}")

        return A_ERROR
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated() | Fired! Reason: $reason")
    }
}