package com.example.avalanche.ui.features.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.store.GetManyStoresRpcKt
import avalanche.merchant.store.Store.GetManyStoresRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.di.services.AvalancheIdentityService
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoresViewModel constructor(
    private val channel: ManagedChannel,
    private val identity: AvalancheIdentityService
) : ViewModel() {

    private val _stores = MutableStateFlow(listOf<GetManyStoresRpc.Response>())

    val stores: StateFlow<List<GetManyStoresRpc.Response>>
        get() = _stores.asStateFlow()

    fun loadStores(nameSearch: String) {

        if (nameSearch.isEmpty()) {
            _stores.update {
                emptyList()
            }

            return
        }

        val credentials = BearerTokenCallCredentials(identity.token().toString())

        val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetManyStoresRpcKt.request {
            this.nameSearch = nameSearch
        }

        viewModelScope.launch {

            val flow = service.getMany(request)

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
}
