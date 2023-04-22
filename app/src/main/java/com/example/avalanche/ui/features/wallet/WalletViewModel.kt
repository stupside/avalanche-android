package com.example.avalanche.ui.features.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.vault.ticket.Ticket.GetManyTicketsRpc
import avalanche.vault.ticket.TicketServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.di.services.AvalancheIdentityService
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel constructor(
    private val channel: ManagedChannel,
    private val identity: AvalancheIdentityService
) : ViewModel() {


    private val _tickets =
        MutableStateFlow(listOf<GetManyTicketsRpc.Response>())

    val tickets: StateFlow<List<GetManyTicketsRpc.Response>>
        get() = _tickets.asStateFlow()

    fun loadTickets() {

        val credentials = BearerTokenCallCredentials(identity.token().toString())

        val service = TicketServiceGrpcKt.TicketServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetManyTicketsRpc.Request.newBuilder()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            _tickets.update {
                emptyList()
            }

            flow.collect { ticket ->

                if (_tickets.value.contains(ticket)) return@collect

                _tickets.update {
                    it + ticket
                }
            }
        }
    }
}