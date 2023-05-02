package com.example.avalanche.ui.drm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.drm.auth.AcceptChallengeRpcKt
import avalanche.drm.auth.Auth
import avalanche.drm.auth.AuthServiceGrpcKt
import avalanche.drm.auth.WatchChallengeRpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ReaderViewModel(
    private val channel: ManagedChannel, private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _challengeId = MutableLiveData<String?>(null)

    val challengeId: LiveData<String?>
        get() = _challengeId

    private val _flow = MutableStateFlow<Auth.WatchChallengeRpc.Response?>(null)

    val flow: StateFlow<Auth.WatchChallengeRpc.Response?>
        get() = _flow.asStateFlow()

    private var _job: Job? = null

    fun prepareChallenge(storeId: String): String {

        val service = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = AcceptChallengeRpcKt.command {
            this.storeId = storeId
        }


        Log.i("ReaderViewModel", "prepareChallenge() | storeId: $storeId")

        val response = runBlocking {

            val response = service.accept(request)

            Log.i("ReaderViewModel", "prepareChallenge() | challengeId: ${response.challengeId}")

            response
        }

        viewModelScope.launch {

            _flow.value = null

            _challengeId.value = response.challengeId
        }

        return response.challengeId
    }

    fun watchChallenge(challengeId: String) {

        _job?.cancel()

        val service = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = WatchChallengeRpcKt.command {
            this.challengeId = challengeId
        }

        _job = viewModelScope.launch {

            val response = service.watch(request)

            Log.i("ReaderViewModel", "watchChallenge() | challengeId: $challengeId")

            response.cancellable().collect {

                Log.i("ReaderViewModel", "watchChallenge() | ${it.message} ${it.success}")

                _flow.value = it
            }
        }
    }
}