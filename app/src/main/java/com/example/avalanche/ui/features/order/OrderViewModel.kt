package com.example.avalanche.ui.features.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.plan.GetOnePlanRpcKt
import avalanche.merchant.plan.OrderPlanRpcKt
import avalanche.merchant.plan.Plan.GetOnePlanRpc
import avalanche.merchant.plan.PlanServiceGrpcKt
import avalanche.merchant.store.GetOneStoreRpcKt
import avalanche.merchant.store.Store.GetOneStoreRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class OrderViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _plan = MutableLiveData<GetOnePlanRpc.Response>()

    val plan: LiveData<GetOnePlanRpc.Response>
        get() = _plan;

    private val _store = MediatorLiveData<GetOneStoreRpc.Response>()

    val store: LiveData<GetOneStoreRpc.Response>
        get() = _store

    init {

        val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        _store.addSource(_plan) {

            val request = GetOneStoreRpcKt.request {
                this.storeId = it.storeId
            }

            viewModelScope.launch {
                _store.value = service.getOne(request)
            }
        }
    }

    fun loadPlan(planId: String) {
        val service = PlanServiceGrpcKt.PlanServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetOnePlanRpcKt.request {
            this.planId = planId
        }

        viewModelScope.launch {
            _plan.value = service.getOne(request)
        }
    }

    fun orderPlan(
        planId: String,
        availableInDays: Int,
        onOrdered: () -> Unit
    ) {
        val service = PlanServiceGrpcKt.PlanServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = OrderPlanRpcKt.command {
            this.planId = planId
            this.availableInDays = availableInDays
        }

        viewModelScope.launch {
            service.order(request)

            onOrdered()
        }
    }
}