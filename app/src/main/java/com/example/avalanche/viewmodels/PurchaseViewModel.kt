package com.example.avalanche.viewmodels

import Avalanche.Sales.OrderService
import Avalanche.Sales.OrderServiceProtoGrpcKt
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.core.grpc.AvalancheChannel
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.AvalancheIdentityState
import kotlinx.coroutines.launch

class PurchaseViewModel : ViewModel() {

    fun purchase(context: Context, ticketName: String, planId: String, availableInDays: Int) {
        val state = AvalancheIdentityState.getInstance(context)

        val channel = AvalancheChannel.getNext()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = OrderServiceProtoGrpcKt.OrderServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = OrderService.IntentPaymentProto.Request.newBuilder()
            .setTicketName(ticketName)
            .setPlanId(planId)
            .setAvailableInDays(availableInDays)

        viewModelScope.launch {
            val response = service.intent(request.build())
        }
    }
}