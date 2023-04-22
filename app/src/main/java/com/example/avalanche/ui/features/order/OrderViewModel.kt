package com.example.avalanche.ui.features.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.plan.OrderPlanRpcKt
import avalanche.merchant.plan.PlanServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.di.services.AvalancheIdentityService
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class OrderViewModel constructor(
    private val channel: ManagedChannel,
    private val identity: AvalancheIdentityService
) : ViewModel() {

    fun orderPlan(
        planId: String,
        availableInDays: Int,
    ) {
        val credentials = BearerTokenCallCredentials(identity.token().toString())

        val service = PlanServiceGrpcKt.PlanServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = OrderPlanRpcKt.command {

            this.planId = planId
            this.availableInDays = availableInDays
        }

        viewModelScope.launch {
            service.order(request)
        }
    }
}