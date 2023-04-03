package com.example.avalanche.views.payment

import Avalanche.Passport.TicketService
import Avalanche.Passport.TicketServiceProtoGrpcKt
import Avalanche.Sales.OrderService
import Avalanche.Sales.OrderServiceProtoGrpcKt
import android.content.Context
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

class PaymentViewModel : ViewModel() {

    private val _tickets =
        MutableStateFlow(listOf<TicketService.GetTicketsProto.Response>())

    val tickets: StateFlow<List<TicketService.GetTicketsProto.Response>>
        get() = _tickets.asStateFlow()

    fun purchase(context: Context, ticketId: String, planId: String, availableInDays: Int, onPurchase: (orderId: String) -> Unit) {
        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = OrderServiceProtoGrpcKt.OrderServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = OrderService.IntentPaymentProto.Request.newBuilder()
            .setTicketId(ticketId)
            .setPlanId(planId)
            .setAvailableInDays(availableInDays)

        viewModelScope.launch {
            val response =service.intent(request.build())

            onPurchase(response.orderId)
        }
    }

    fun receive(context: Context, orderId: String, amount: Int, onReceived: (isPaymentFullFilled: Boolean) -> Unit) {
        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = OrderServiceProtoGrpcKt.OrderServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = OrderService.ReceivePaymentProto.Request.newBuilder().setOrderId(orderId).setAmount(amount)

        viewModelScope.launch {
            val response = service.receive(request.build())

            onReceived(response.isPaymentFullFilled)
        }
    }

    fun loadTickets(context: Context, storeId: String) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.GetTicketsProto.Request.newBuilder().setStoreId(storeId)

        viewModelScope.launch {

            val flow = service.getMany(request.build())

            _tickets.update {
                emptyList()
            }

            flow.collect { ticket ->

                if (_tickets.value.contains(ticket)) return@collect

                _tickets.update {
                    it + ticket
                }
            }
        }
    }

    fun createTicket(
        context: Context,
        storeId: String,
        ticketName: String,
        onCreated: (ticketId: String) -> Unit
    ) {

        val state = AvalancheIdentityState.getInstance(context).readState()

        val channel = AvalancheChannel.getNew()

        val credentials =
            BearerTokenCallCredentials(state.accessToken.toString())

        val service = TicketServiceProtoGrpcKt.TicketServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = TicketService.CreateTicketProto.Request.newBuilder()
            .setStoreId(storeId)
            .setName(ticketName)

        viewModelScope.launch {
            val response = service.create(request.build())

            onCreated(response.ticketId)
        }
    }
}