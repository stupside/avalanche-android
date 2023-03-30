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
import com.example.avalanche.shared.Constants
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class StoresViewModel : ViewModel() {

    private val _stores =
        MutableLiveData(mutableListOf<StoreService.GetStoresProto.Response>())

    val stations: LiveData<MutableList<StoreService.GetStoresProto.Response>>
        get() = _stores

    fun loadStores(context: Context, name: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(name)

        _stores.value?.clear()

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            flow.collect { store ->

                if (_stores.value == null) {
                    _stores.value = mutableListOf(store)
                } else {
                    _stores.value!!.add(store)
                }
            }
        }
    }
}
