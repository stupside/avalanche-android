package com.example.avalanche.ui.features.store

import PlanItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import avalanche.merchant.plan.Plan
import avalanche.merchant.store.Store
import com.example.avalanche.ui.components.AvalancheCard
import com.example.avalanche.ui.components.AvalancheGoBackButton
import com.example.avalanche.ui.components.list.AvalancheList

@Composable
fun StoreView(
    viewModel: StoreViewModel,
    storeId: String,
    goBack: () -> Unit,
    goOrder: (planId: String) -> Unit
) {

    val store: Store.GetOneStoreRpc.Response? by viewModel.store.observeAsState()
    val plans: Plan.GetManyPlansRpc.Response? by viewModel.plans.observeAsState()

    LaunchedEffect(storeId) {
        viewModel.getStore(storeId)
    }

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

                store?.let {

                    AvalancheCard(
                        name = it.name,
                        description = it.description,
                        logo = it.logo.value
                    )
                }

                plans?.let {

                    AvalancheList(elements = it.itemsList) { plan ->
                        PlanItem(
                            name = plan.name,
                            description = plan.description,
                            price = plan.price,
                            free = plan.isFree,
                            duration = plan.duration.seconds,
                            onClick = {
                                goOrder(plan.planId)
                            })
                    }
                }
            }
        }
    }
}