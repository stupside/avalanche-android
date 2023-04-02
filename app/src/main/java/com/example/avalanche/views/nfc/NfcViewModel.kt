package com.example.avalanche.views.nfc

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
import kotlinx.coroutines.launch

class NfcViewModel : ViewModel() {

    private val _store = MutableLiveData<StoreService.GetStoreProto.Response>()

    val store: LiveData<StoreService.GetStoreProto.Response>
        get() = _store

    private val _ticketId = MutableLiveData<String>()

    val ticketId: LiveData<String>
        get() = _ticketId

    private val _ticket = MutableLiveData<TicketService.GetTicketProto.Response>()

    val ticket: LiveData<TicketService.GetTicketProto.Response>
        get() = _ticket

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

    fun loadTicket(context: Context, storeId: String) {
        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val seal = service.getSeal(
                TicketService.GetSealProto.Request.newBuilder().setStoreId(storeId).build()
            )

            _ticketId.value = seal.ticketId

            val ticket = service.getOne(
                TicketService.GetTicketProto.Request.newBuilder().setTicketId(seal.ticketId).build()
            )

            _ticket.value = ticket
        }
    }

}