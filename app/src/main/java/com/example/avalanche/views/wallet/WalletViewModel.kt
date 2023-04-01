package com.example.avalanche.views.wallet

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
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

class WalletViewModel : ViewModel() {

    private val _tickets =
        MutableStateFlow(listOf<TicketService.GetTicketsProto.Response>())

    private val _store = MutableLiveData<StoreService.GetStoreProto.Response>()


    val tickets: StateFlow<List<TicketService.GetTicketsProto.Response>>
        get() = _tickets.asStateFlow()

    val store: LiveData<StoreService.GetStoreProto.Response>
        get() = _store

    fun loadTickets(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetTicketsProto.Request.newBuilder().setStoreId(storeId)

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            _tickets.update {
                emptyList()
            }

            flow.collect { ticket ->

                if (_tickets.value.contains(ticket)) return@collect

                _tickets.update {
                    it + ticket
                }
            }
        }
    }

    fun loadStore(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = StoreService.GetStoreProto.Request.newBuilder().setStoreId(storeId)

            val store = service.getOne(request.build())

            _store.value = store
        }
    }
}