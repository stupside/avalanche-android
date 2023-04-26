package com.example.avalanche.ui.features.stores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.store.GetManyStoresRpcKt
import avalanche.merchant.store.Store.GetManyStoresRpc
import avalanche.merchant.store.StoreServiceGrpcKt
import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch

class StoresViewModel constructor(
    private val channel: ManagedChannel, private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _stores = MutableLiveData<GetManyStoresRpc.Response>()

    val stores: LiveData<GetManyStoresRpc.Response>
        get() = _stores

    fun loadStores(nameSearch: String) {

        val service =
            StoreServiceGrpcKt.StoreServiceCoroutineStub(channel).withCallCredentials(credentials)

        val request = GetManyStoresRpcKt.request {
            this.nameSearch = nameSearch
        }

        viewModelScope.launch {

            _stores.value = service.getMany(request)
        }
    }
}
