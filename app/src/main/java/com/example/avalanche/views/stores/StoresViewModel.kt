package com.example.avalanche.views.stores

import Avalanche.Market.PlanService
import Avalanche.Market.PlanServiceProtoGrpcKt
import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
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

class StoresViewModel : ViewModel() {

    private val _stores = MutableStateFlow(listOf<StoreService.GetStoresProto.Response>())
    private val _store = MutableLiveData<StoreService.GetStoreProto.Response>()
    private val _plans = MutableStateFlow(listOf<PlanService.GetPlansProto.Response>())

    val stores: StateFlow<List<StoreService.GetStoresProto.Response>>
        get() = _stores.asStateFlow()

    val store: LiveData<StoreService.GetStoreProto.Response>
        get() = _store

    val plans: StateFlow<List<PlanService.GetPlansProto.Response>>
        get() = _plans.asStateFlow()

    fun loadStores(context: Context, nameSearch: String) {

        if (nameSearch.isEmpty()) {
            _stores.update {
                emptyList()
            }

            return
        }

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = StoreService.GetStoresProto.Request.newBuilder().setNameSearch(nameSearch)

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            _stores.update {
                emptyList()
            }

            flow.collect { store ->

                if (_stores.value.contains(store)) return@collect

                _stores.update {
                    it + store
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

    fun loadPlans(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = PlanServiceProtoGrpcKt.PlanServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = PlanService.GetPlansProto.Request.newBuilder().setStoreId(storeId)

        viewModelScope.launch {


            val flow = service.getMany(request.build())

            _plans.update {
                emptyList()
            }

            flow.collect { plan ->
                if (_plans.value.contains(plan)) return@collect

                _plans.update {
                    it + plan
                }
            }
        }
    }
}
