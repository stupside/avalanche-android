package com.example.avalanche.ui.features.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.store.GetOneStoreRpcKt
import avalanche.merchant.store.Store.GetOneStoreRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import avalanche.vault.ticket.GetOneTicketRpcKt
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class TicketViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _ticket = MutableLiveData<GetOneTicketRpc.Response>()

    val ticket: LiveData<GetOneTicketRpc.Response>
        get() = _ticket


    private val _store = MediatorLiveData<GetOneStoreRpc.Response>()

    val store: LiveData<GetOneStoreRpc.Response>
        get() = _store

    init {
        val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        _store.addSource(_ticket) {

            val request = GetOneStoreRpcKt.request {
                this.storeId = it.storeId
            }

            viewModelScope.launch {
                _store.value = service.getOne(request)
            }
        }
    }

    fun setTicketId(ticketId: String) {

        val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetOneTicketRpcKt.request {
            this.ticketId = ticketId
        }

        viewModelScope.launch {

            val ticket = service.getOne(request)

            _ticket.value = ticket
        }
    }
}
