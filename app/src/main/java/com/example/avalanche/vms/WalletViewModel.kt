package com.example.avalanche.vms

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.grpc.BearerTokenCallCredentials
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.shared.Constants
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class WalletViewModel(private val storeId: String) : ViewModel() {

    private val _tickets =
        MutableLiveData(emptyList<TicketService.GetTicketsProto.Response>().toMutableList())

    val tickets: LiveData<MutableList<TicketService.GetTicketsProto.Response>>
        get() = _tickets

    fun loadWallet(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetTicketsProto.Request.newBuilder().setStoreId(storeId)

        _tickets.value?.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { ticket ->

                if (_tickets.value == null) {
                    _tickets.value = mutableListOf(ticket)
                } else {
                    _tickets.value!!.add(ticket)
                }
            }
        }
    }
}