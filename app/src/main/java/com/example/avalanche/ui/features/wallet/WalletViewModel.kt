package com.example.avalanche.ui.features.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.vault.ticket.Ticket.GetManyTicketsRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class WalletViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _tickets = MutableLiveData<GetManyTicketsRpc.Response>()

    val tickets: LiveData<GetManyTicketsRpc.Response>
        get() = _tickets

    fun loadTickets() {

        val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetManyTicketsRpc.Request.newBuilder()

        viewModelScope.launch {

            _tickets.value = service.getMany(request.build())
        }
    }
}