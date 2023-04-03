package com.example.avalanche.views.wallets

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletsViewModel : ViewModel() {

    private val _wallets =
        MutableStateFlow(listOf<TicketService.GetWalletsProto.Response>())

    val wallets: StateFlow<List<TicketService.GetWalletsProto.Response>>
        get() = _wallets.asStateFlow()

    fun loadWallets(context: Context) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetWalletsProto.Request.newBuilder()

        viewModelScope.launch {

            val flow = service.getWallets(request.build())

            _wallets.update {
                emptyList()
            }

            flow.collect { wallet ->

                if (_wallets.value.contains(wallet)) return@collect

                _wallets.update {
                    it + wallet
                }
            }
        }
    }

    private val _ticketId = MutableLiveData<String>()

    val ticketId: LiveData<String>
        get() = _ticketId

    private val _ticket = MutableLiveData<TicketService.GetTicketProto.Response>()

    val ticket: LiveData<TicketService.GetTicketProto.Response>
        get() = _ticket

    fun loadTicket(context: Context, storeId: String, deviceIdentifier: String) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val seal = service.getSeal(
                TicketService.GetSealProto.Request.newBuilder().setStoreId(storeId)
                    .setDeviceIdentifier(deviceIdentifier).build()
            )

            _ticketId.value = seal.ticketId.value

            _ticketId.value?.let {ticketId ->

                val ticket = service.getOne(
                    TicketService.GetTicketProto.Request.newBuilder()
                        .setTicketId(ticketId).build()
                )

                _ticket.value = ticket
            }
        }
    }
}
