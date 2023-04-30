package com.example.reader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import avalanche.drm.auth.AcceptChallengeRpcKt
import avalanche.drm.auth.AuthServiceGrpcKt
import avalanche.drm.auth.WatchChallengeRpcKt
import com.example.core.environment.Constants
import com.example.core.grpc.BearerTokenCallCredentials
import com.example.reader.theme.AvalancheTheme
import io.grpc.ManagedChannel
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class AvalancheActivity : ComponentActivity() {

    private lateinit var nfc: NfcAdapter
    private lateinit var callback: AvalancheReaderCallback

    private val channel: ManagedChannel by inject()
    private val credentials: BearerTokenCallCredentials by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = "test-test-test"

        nfc = NfcAdapter.getDefaultAdapter(this)

        setContent {

            callback = AvalancheReaderCallback(storeId, channel, credentials) {

                val service = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
                    .withCallCredentials(credentials)

                val request = WatchChallengeRpcKt.command {
                    this.challengeId = it
                }

                val response = service.watch(request)

                runBlocking {

                    response.collect { response ->
                        Log.i(
                            "AvalancheActivity",
                            "drm response: ${response.message} -> ${response.success}"
                        )
                    }
                }
            }

            AvalancheTheme {

                AvalancheNavHost()
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


class AvalancheReaderCallback constructor(
    private val storeId: String,
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials,
    private val onAcceptChallenge: (challengeId: String) -> Unit

) : NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag?) {

        val iso = IsoDep.get(tag)

        iso.connect()

        iso.transceive(Constants.SELECT_APDU)

        Log.i("AvalancheReaderCallback", "onTagDiscovered()")

        val service = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = AcceptChallengeRpcKt.command {
            this.storeId = this@AvalancheReaderCallback.storeId
        }

        val response = runBlocking {

            service.accept(request)
        }

        onAcceptChallenge(response.challengeId)

        iso.transceive(response.challengeId.encodeToByteArray())

        iso.close()
    }
}