package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpc
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avalanche.ui.theme.AvalancheTheme
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.lifecycle.*

class StationViewModel : ViewModel() {
    fun getStation(stationId: String) {
        viewModelScope.launch {
            val channel =
                ManagedChannelBuilder.forAddress("grpc://localhost", 8081).usePlaintext().build()
            val storeService = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            val request = StoreService.GetStoreProto.Request.newBuilder().setStoreId(stationId)

            val store = storeService.getOne(request.build())

        }
    }
}

class StationInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AvalancheTheme() {
            }

        }

    }
}