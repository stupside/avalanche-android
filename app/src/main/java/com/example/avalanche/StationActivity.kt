package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.grpc.BearerTokenCallCredentials
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch

class StationViewModel : ViewModel() {

    private val _data = MutableLiveData<StoreService.GetStoreProto.Response>()

    val data: LiveData<StoreService.GetStoreProto.Response>
        get() = _data

    fun load(context: Context, stationId: String) {

        val address = "grpc://localhost:8081"

        val state = AvalancheIdentityState.getInstance(context)

        val channel = ManagedChannelBuilder.forTarget(address).usePlaintext().build()

        val credentials =
            BearerTokenCallCredentials(state.get().accessToken.toString())

        val service = StoreServiceProtoGrpcKt.StoreServiceProtoCoroutineStub(channel)
            .withCallCredentials(credentials)

        viewModelScope.launch {

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

        vm.load(this, id)

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