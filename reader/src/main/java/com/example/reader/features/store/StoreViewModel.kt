package com.example.reader.features.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avalanche.merchant.store.GetOneStoreRpcKt
import avalanche.merchant.store.Store
import avalanche.merchant.store.StoreServiceGrpcKt
import com.example.core.grpc.BearerTokenCallCredentials
import io.grpc.ManagedChannel
import kotlinx.coroutines.launch


class StoreViewModel constructor(
    private val channel: ManagedChannel,
    private val credentials: BearerTokenCallCredentials
) : ViewModel() {

    private val _store = MutableLiveData<Store.GetOneStoreRpc.Response>()

    val store: LiveData<Store.GetOneStoreRpc.Response>
        get() = _store

    fun getStore(storeId: String) {

        val service = StoreServiceGrpcKt.StoreServiceCoroutineStub(channel)
            .withCallCredentials(credentials)

        val request = GetOneStoreRpcKt.request {
            this.storeId = storeId
        }

        viewModelScope.launch {

            val ticket = service.getOne(request)

            _store.value = ticket
        }
    }
}