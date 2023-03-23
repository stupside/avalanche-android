package com.example.avalanche.vms

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.grpc.BearerTokenCallCredentials
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.shared.Constants
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

class StationsViewModel : ViewModel() {

    private val _data =
        MutableLiveData(emptyList<StoreService.GetStoresProto.Response>().toMutableList())

    val data: LiveData<MutableList<StoreService.GetStoresProto.Response>>
        get() = _data

    fun load(context: Context, name: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.MARKET_SERVICE).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(name)

        _data.value?.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { store ->
                _data.value = _data.value?.plus(store)?.toMutableList() ?: mutableListOf(store)
            }
        }
    }
}
