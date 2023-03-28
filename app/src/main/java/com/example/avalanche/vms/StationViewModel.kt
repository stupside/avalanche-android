package com.example.avalanche.vms

import Avalanche.Market.PlanService
import Avalanche.Market.PlanServiceProtoGrpcKt
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

    private val _store = MutableLiveData<StoreService.GetStoreProto.Response>()
    private val _plans = MutableLiveData<MutableList<PlanService.GetPlansProto.Response>>()

    val store: LiveData<StoreService.GetStoreProto.Response>
        get() = _store

    val plans: LiveData<MutableList<PlanService.GetPlansProto.Response>>
        get() = _plans

    fun loadStore(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = ManagedChannelBuilder.forTarget(Constants.REVERSE_PROXY).usePlaintext().build()

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

    fun loadPlans(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = ManagedChannelBuilder.forTarget(Constants.REVERSE_PROXY).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = PlanServiceProtoGrpcKt.PlanServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

            val request = PlanService.GetPlansProto.Request.newBuilder().setStoreId(storeId)

            val flow = service.getMany(request.build())

            flow.collect { plan ->
                _plans.value = _plans.value?.plus(plan)?.toMutableList() ?: mutableListOf(plan)
            }
        }
    }
}
