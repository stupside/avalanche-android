package com.example.avalanche.viewmodels

import Avalanche.Market.PlanService
import Avalanche.Market.PlanServiceProtoGrpcKt
import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.shared.Constants
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class StoreViewModel(private val storeId: String) : ViewModel() {

    private val _store = MutableLiveData<StoreService.GetStoreProto.Response>()
    private val _plans = MutableLiveData(mutableListOf<PlanService.GetPlansProto.Response>())

    val store: LiveData<StoreService.GetStoreProto.Response>
        get() = _store

    val plans: LiveData<MutableList<PlanService.GetPlansProto.Response>>
        get() = _plans

    fun loadStore(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext().build()

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

    fun loadPlans(context: Context) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel =
            ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = PlanServiceProtoGrpcKt.PlanServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        _plans.value?.clear()

        viewModelScope.launch {

            val request = PlanService.GetPlansProto.Request.newBuilder().setStoreId(storeId)

            val flow = service.getMany(request.build())

            flow.collect { plan ->

                if (_plans.value == null) {
                    _plans.value = mutableListOf(plan)
                } else {
                    _plans.value!!.add(plan)
                }
            }
        }
    }
}
