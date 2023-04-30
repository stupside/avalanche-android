package com.example.reader.features.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.store.Store
import com.example.reader.components.AvalancheGoBackButton
import com.example.reader.components.AvalancheLogo

@Composable
fun StoreView(
    viewModel: StoreViewModel,
    storeId: String,
    goBack: () -> Unit,
) {

    LaunchedEffect(storeId) {
        viewModel.getStore(storeId)
    }

    val store: Store.GetOneStoreRpc.Response? by viewModel.store.observeAsState()

    Scaffold(topBar = {

        TopAppBar(title = {

            store?.let {

                Text(it.name)
            }
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                store?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AvalancheLogo(logo = it.logo.value)
                    }
                }
            }
        }
    }
}