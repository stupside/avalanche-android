package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.*
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class StationViewModel : ViewModel() {

    private val _data = MutableLiveData<StoreService.GetStoreProto.Response>()

    val data: LiveData<StoreService.GetStoreProto.Response>
        get() = _data

    fun load(stationId: String) {

        val address = "grpc://localhost"
        val port = 8081

        viewModelScope.launch {

            val channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()

            val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)

            val request = StoreService.GetStoreProto.Request.newBuilder().setStoreId(stationId)

            val store = service.getOne(request.build())

            _data.value = store
        }
    }
}


class StationActivity : ComponentActivity() {

    private val vm = StationViewModel()

    companion object {
        const val StationIdKey = "StationId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra(StationIdKey)!!

        vm.load(id)

        setContent {

            val store: StoreService.GetStoreProto.Response? by vm.data.observeAsState(null)

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = store?.name.toString(), content = {

                    Text(text = store?.description.toString())
                    Text(text = store?.email.toString())
                    Text(text = store?.logo.toString())

                })
            }, button = {})
        }
    }
}