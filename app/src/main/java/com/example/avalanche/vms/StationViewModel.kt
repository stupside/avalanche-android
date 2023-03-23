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

class StationViewModel : ViewModel() {

    private val _data = MutableLiveData<StoreService.GetStoreProto.Response>()

    val data: LiveData<StoreService.GetStoreProto.Response>
        get() = _data

    fun load(context: Context, stationId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = ManagedChannelBuilder.forTarget(Constants.MARKET_SERVICE).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = StoreService.GetStoreProto.Request.newBuilder().setStoreId(stationId)

            val store = service.getOne(request.build())

            _data.value = store
        }
    }
}
