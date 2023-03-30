package com.example.avalanche.viewmodels

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.AvalancheChannel
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WalletViewModel(private val storeId: String) : ViewModel() {

    private val _tickets =
        MutableStateFlow(emptyList<TicketService.GetTicketsProto.Response>().toMutableList())

    val tickets: StateFlow<MutableList<TicketService.GetTicketsProto.Response>>
        get() = _tickets

    fun loadWallet(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getInstance()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetTicketsProto.Request.newBuilder().setStoreId(storeId)

        _tickets.value.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { ticket ->

                _tickets.value += ticket
            }
        }
    }
}