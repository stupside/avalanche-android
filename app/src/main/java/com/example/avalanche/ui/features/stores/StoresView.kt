package com.example.avalanche.ui.features.stores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.store.Store.GetManyStoresRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton
import com.example.avalanche.ui.components.AvalancheLogo

@Composable
fun StoresView(viewModel: StoresViewModel, goBack: () -> Unit, goStore: (storeId: String) -> Unit) {

    var search by remember { mutableStateOf("") }

    LaunchedEffect(search) {

        if (search.length > 3) {

            viewModel.loadStores(search)
        }
    }

    val stores: GetManyStoresRpc.Response? by viewModel.stores.observeAsState()

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

                TextField(value = search, onValueChange = { search = it }, singleLine = true)

                stores?.let {

                    LazyColumn {

                        for (store in it.itemsList) {

                            item(store.storeId) {

                                ListItem(modifier = Modifier.clickable {
                                    goStore(store.storeId)
                                }, leadingContent = {
                                    AvalancheLogo(logo = store.logo.value)
                                }, headlineContent = {
                                    Text(store.name)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}