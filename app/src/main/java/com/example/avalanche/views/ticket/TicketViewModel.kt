package com.example.avalanche.views.ticket

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.AvalancheChannel
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import kotlinx.coroutines.launch

class TicketViewModel : ViewModel() {

    private val _ticket = MutableLiveData<TicketService.GetTicketProto.Response>()

    val ticket: LiveData<TicketService.GetTicketProto.Response>
        get() = _ticket

    fun loadTicket(context: Context, ticketId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.GetTicketProto.Request.newBuilder().setTicketId(ticketId)

            val ticket = service.getOne(request.build())

            _ticket.value = ticket
        }
    }

    fun sealTicket(context: Context, ticketId: String, deviceId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.SealTicketProto.Request.newBuilder().setTicketId(ticketId)
                .setDeviceIdentifier(deviceId)

            service.seal(request.build())
        }
    }

    fun unsealTicket(context: Context, ticketId: String, deviceId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.UnsealTicketProto.Request.newBuilder().setTicketId(ticketId)
                .setDeviceIdentifier(deviceId)

            service.unseal(request.build())
        }
    }
}
