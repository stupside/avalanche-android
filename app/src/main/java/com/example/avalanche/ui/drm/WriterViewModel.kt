package com.example.avalanche.ui.drm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.drm.auth.AcquireChallengeRpcKt
import avalanche.drm.auth.Auth.AcquireChallengeRpc
import avalanche.drm.auth.AuthServiceGrpcKt
import avalanche.merchant.store.GetOneStoreRpcKt
import avalanche.merchant.store.Store.GetOneStoreRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import avalanche.vault.ticket.GetOneTicketRpcKt
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class WriterViewModel(
    private val channel: ManagedChannel, private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _flow = MutableStateFlow<AcquireChallengeRpc.Response?>(null)

    val flow: StateFlow<AcquireChallengeRpc.Response?>
        get() = _flow.asStateFlow()

    private var _job: Job? = null

    private val _ticket = MutableLiveData<GetOneTicketRpc.Response>()

    val ticket: LiveData<GetOneTicketRpc.Response>
        get() = _ticket

    private val _store = MediatorLiveData<GetOneStoreRpc.Response>()

    val store: LiveData<GetOneStoreRpc.Response>
        get() = _store

    init {

        _store.addSource(_ticket) {
            val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)

            val request = GetOneStoreRpcKt.request {
                this.storeId = it.storeId
            }

            viewModelScope.launch {

                _store.value = service.getOne(request)
            }
        }

        viewModelScope.launch {

            val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
                .withCallCredentials(credentials)

            _flow.collect { response ->

                response?.let {

                    if (it.success) {

                        val request = GetOneTicketRpcKt.request {
                            this.ticketId = it.ticketId.value
                        }

                        _ticket.value = service.getOne(request)
                    }
                }
            }
        }
    }

    fun challenge(challengeId: String) {

        _job?.cancel()

        val service =
            AuthServiceGrpcKt.AuthServiceCoroutineStub(channel).withCallCredentials(credentials)

        val request = AcquireChallengeRpcKt.command {
            this.challengeId = challengeId
        }

        _job = viewModelScope.launch {

            val response = service.acquire(request)

            Log.i("WriterViewModel", "challenge() | challengeId: $challengeId")

            response.cancellable().collect {

                Log.i("WriterViewModel", "challenge() | ${it.message} ${it.success}")

                _flow.value = it
            }
        }
    }
}