package com.example.avalanche.ui.features.stores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.store.Store.GetManyStoresRpc
import com.example.avalanche.ui.components.AvalancheCard
import com.example.avalanche.ui.components.AvalancheGoBackButton
import com.example.avalanche.ui.components.list.AvalancheList

@Composable
fun StoresView(viewModel: StoresViewModel, goBack: () -> Unit, goStore: (storeId: String) -> Unit) {

    val stores: GetManyStoresRpc.Response? by viewModel.stores.observeAsState()

    val search by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(search) {
        if (search.isNullOrEmpty())
            viewModel.loadStores(search!!)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Stores")
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                stores?.let {

                    AvalancheList(elements = it.itemsList) { store ->

                        AvalancheCard(
                            name = store.name,
                            description = store.description,
                            logo = store.logo.value,
                            modifier = Modifier.clickable {
                                goStore(store.storeId)
                            }
                        )
                    }
                }
            }
        }
    }
}