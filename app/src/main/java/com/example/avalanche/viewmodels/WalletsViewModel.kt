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

class WalletsViewModel : ViewModel() {

    private val _wallets =
        MutableStateFlow(mutableListOf<TicketService.GetWalletsProto.Response>())

    val wallets: StateFlow<MutableList<TicketService.GetWalletsProto.Response>>
        get() = _wallets

    fun loadWallets(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNext()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetWalletsProto.Request.newBuilder()

        viewModelScope.launch {

            val flow = service.getWallets(request.build())

            flow.collect { wallet ->

                if (_wallets.value.contains(wallet)) return@collect

                _wallets.value += wallet
            }
        }
    }
}
