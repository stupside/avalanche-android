package com.example.avalanche.ui.features.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.plan.GetManyPlansRpcKt
import avalanche.merchant.plan.Plan
import avalanche.merchant.plan.PlanServiceGrpcKt
import avalanche.merchant.store.GetOneStoreRpcKt
import avalanche.merchant.store.Store
import avalanche.merchant.store.StoreServiceGrpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch


class StoreViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _store = MutableLiveData<Store.GetOneStoreRpc.Response>()

    val store: LiveData<Store.GetOneStoreRpc.Response>
        get() = _store

    private val _plans = MediatorLiveData<Plan.GetManyPlansRpc.Response>()

    val plans: LiveData<Plan.GetManyPlansRpc.Response>
        get() = _plans

    init {
        _plans.addSource(store) {
            val service = PlanServiceGrpcKt.PlanServiceCoroutineStub(channel)
                .withCallCredentials(credentials)

            val request = GetManyPlansRpcKt.request {
                this.storeId = it.storeId
            }

            viewModelScope.launch {
                _plans.value = service.getMany(request)
            }
        }
    }

    fun getStore(storeId: String) {

        val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetOneStoreRpcKt.request {
            this.storeId = storeId
        }

        viewModelScope.launch {

            val ticket = service.getOne(request)

            _store.value = ticket
        }
    }
}