package com.example.avalanche.ui.features.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.store.GetManyStoresRpcKt
import avalanche.merchant.store.Store.GetManyStoresRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import avalanche.vault.ticket.Ticket.GetManyTicketsRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class WalletViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _tickets = MutableLiveData<GetManyTicketsRpc.Response>()

    val tickets: LiveData<GetManyTicketsRpc.Response>
        get() = _tickets

    private val _stores = MediatorLiveData<GetManyStoresRpc.Response>()

    val stores: LiveData<GetManyStoresRpc.Response>
        get() = _stores

    init {

        _stores.addSource(_tickets) {

            val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
                .withCallCredentials(credentials)

            val request = GetManyStoresRpcKt.requestByIdentifiers {

                for (ticket in it.itemsList) {
                    this.identifiers.add(StringValue.of(ticket.storeId))
                }
            }

            viewModelScope.launch {

                _stores.value = service.getManyByIdentifiers(request)
            }
        }
    }

    fun loadTickets() {

        val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetManyTicketsRpc.Request.newBuilder()

        viewModelScope.launch {

            _tickets.value = service.getMany(request.build())
        }
    }
}