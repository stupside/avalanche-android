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

    private val _seal = MutableLiveData<Boolean>()

    val seal: LiveData<Boolean>
        get() = _seal

    fun loadTicket(context: Context, ticketId: String, deviceIdentifier: String) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.GetTicketProto.Request.newBuilder().setTicketId(ticketId)

            val ticket = service.getOne(request.build())

            _ticket.value = ticket

            ticket.let {

                val seal = service.getSeal(
                    TicketService.GetSealProto.Request.newBuilder()
                        .setDeviceIdentifier(deviceIdentifier).setStoreId(it.storeId).build()
                )

                _seal.value = ticketId == seal.ticketId.value
            }
        }
    }

    fun sealTicket(context: Context, storeId: String, ticketId: String, deviceIdentifier: String) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.SealTicketProto.Request.newBuilder()
                .setTicketId(ticketId)
                .setStoreId(storeId)
                .setDeviceIdentifier(deviceIdentifier)

            service.seal(request.build())
        }
    }

    fun unsealTicket(context: Context, ticketId: String, deviceIdentifier: String) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = TicketService.UnsealTicketProto.Request.newBuilder()
                .setTicketId(ticketId)
                .setDeviceIdentifier(deviceIdentifier)

            service.unseal(request.build())
        }
    }
}
