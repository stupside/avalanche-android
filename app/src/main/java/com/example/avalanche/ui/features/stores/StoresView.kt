package com.example.avalanche.ui.features.stores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.store.Store.GetManyStoresRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton

@Composable
fun StoresView(viewModel: StoresViewModel, goBack: () -> Unit) {

    val stores: List<GetManyStoresRpc.Response> by viewModel.stores.collectAsState()

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

            }
        }
    }
}