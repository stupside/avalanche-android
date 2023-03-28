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

class WalletViewModel: ViewModel() {

    private val _data =
        MutableLiveData(emptyList<TicketService.GetTicketsProto.Response>().toMutableList())

    val data: LiveData<MutableList<TicketService.GetTicketsProto.Response>>
        get() = _data

    fun loadWallet(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.REVERSE_PROXY).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetTicketsProto.Request.newBuilder()

        _data.value?.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { data ->
                _data.value = _data.value?.plus(data)?.toMutableList() ?: mutableListOf(data)
            }
        }
    }
}