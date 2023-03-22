package com.example.avalanche

import Avalanche.Market.StoreService
import Avalanche.Market.StoreServiceProtoGrpcKt
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.grpc.BearerTokenCallCredentials
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.identity.AvalancheIdentityViewModel
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.list.AvalancheList
import com.example.avalanche.ui.shared.list.AvalancheListElement
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.StationsViewModel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

class StationsActivity : ComponentActivity() {

    private val vm: StationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var search by remember { mutableStateOf("") }

            if (search.isNotEmpty())
                vm.load(this, search)

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = "Search") {

                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Name") }
                    )

                    val stores: List<StoreService.GetStoresProto.Response> by vm.data.observeAsState(
                        emptyList()
                    )

                    AvalancheList(elements = stores, template = { store ->
                        AvalancheListElement(
                            image = store.logo.toString(),
                            description = store.description,
                            onClick = {
                                // TODO: start StationActivity
                            },
                            content = {
                            })
                    })
                }
            }, button = {})
        }
    }
}