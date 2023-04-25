package com.example.avalanche.ui.features.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.store.Store
import com.example.avalanche.ui.components.AvalancheGoBackButton

@Composable
fun StoreView(viewModel: StoreViewModel, storeId: String, goBack: () -> Unit) {

    val store: Store.GetOneStoreRpc.Response? by viewModel.store.observeAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Store")
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

            }
        }
    }
}