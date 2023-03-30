package com.example.avalanche.viewmodels

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.shared.Constants
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class WalletsViewModel : ViewModel() {

    private val _wallets =
        MutableLiveData(mutableListOf<TicketService.GetWalletsProto.Response>())

    val wallets: LiveData<MutableList<TicketService.GetWalletsProto.Response>>
        get() = _wallets

    fun loadWallets(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetWalletsProto.Request.newBuilder()

        _wallets.value?.clear()

        viewModelScope.launch {

            val flow = service.getWallets(request.build())

            flow.collect { wallet ->

                if (_wallets.value == null) {
                    _wallets.value = mutableListOf(wallet)
                } else {
                    _wallets.value!!.add(wallet)
                }
            }
        }
    }
}
