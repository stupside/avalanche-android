package com.example.avalanche.views.wallets

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
}
