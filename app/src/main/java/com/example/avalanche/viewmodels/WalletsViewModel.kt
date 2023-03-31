package com.example.avalanche.viewmodels

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.AvalancheChannel
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WalletsViewModel : ViewModel() {

    private val _wallets =
        MutableStateFlow(listOf<TicketService.GetWalletsProto.Response>())

    val wallets: StateFlow<List<TicketService.GetWalletsProto.Response>>
        get() = _wallets.asStateFlow()

    fun loadWallets(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetWalletsProto.Request.newBuilder()

        _wallets.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

        viewModelScope.launch(Dispatchers.IO) {

            val flow = service.getWallets(request.build())

            flow.collect { wallet ->

                if (_wallets.value.contains(wallet)) return@collect

                _wallets.update {
                    it + wallet
                }
            }
        }
    }
}
