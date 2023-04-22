package com.example.avalanche.ui.features.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.vault.ticket.Ticket.GetOneTicketRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.di.services.AvalancheIdentityService
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class TicketViewModel constructor(
    private val channel: ManagedChannel,
    private val identity: AvalancheIdentityService
) : ViewModel() {

    private val _ticket = MutableLiveData<GetOneTicketRpc.Response>()

    val ticket: LiveData<GetOneTicketRpc.Response>
        get() = _ticket

    fun setTicketId(ticketId: String) {

        val credentials = BearerTokenCallCredentials(identity.token().toString())

        val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = GetOneTicketRpc.Request.newBuilder().setTicketId(ticketId)

            val ticket = service.getOne(request.build())

            _ticket.value = ticket
        }
    }
}
