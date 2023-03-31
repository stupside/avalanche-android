package com.example.avalanche.viewmodels

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
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

class StoresViewModel : ViewModel() {

    private val _stores = MutableStateFlow(listOf<StoreService.GetStoresProto.Response>())

    val stores: StateFlow<List<StoreService.GetStoresProto.Response>>
        get() = _stores.asStateFlow()

    fun loadStores(context: Context, nameSearch: String) {

        if (nameSearch.isEmpty()) return;

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(nameSearch)

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { store ->

                if (_stores.value.contains(store)) return@collect

                _stores.update {
                    it + store
                }
            }
        }
    }
}
