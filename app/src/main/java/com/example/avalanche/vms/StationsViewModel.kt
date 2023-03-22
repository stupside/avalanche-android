package com.example.avalanche.vms

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.grpc.BearerTokenCallCredentials
import com.example.avalanche.identity.AvalancheIdentityState
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

class StationsViewModel : ViewModel() {

    private val _data =
        MutableLiveData(emptyList<StoreService.GetStoresProto.Response>().toMutableList())

    val data: LiveData<MutableList<StoreService.GetStoresProto.Response>>
        get() = _data

    fun load(context: Context, name: String) {

        val address = "grpc://localhost:8081"

        val state = AvalancheIdentityState.getInstance(context)

        val channel = ManagedChannelBuilder.forTarget(address).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(name)

        _data.value?.clear()

        viewModelScope.launch {

            val store = service.getMany(request.build())

            _data.value = store.toCollection(_data.value!!)
        }
    }
}
