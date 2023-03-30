package com.example.avalanche.viewmodels

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.envrionment.Constants
import com.example.avalanche.core.grpc.AvalancheChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

class StoresViewModel : ViewModel() {

    private val _stores = MutableStateFlow(mutableListOf<StoreService.GetStoresProto.Response>())

    val stores: StateFlow<MutableList<StoreService.GetStoresProto.Response>>
        get() = _stores

    fun loadStores(context: Context, name: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getInstance()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(name)

        _stores.value.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { store ->
                _stores.value += store
            }
        }
    }
}
