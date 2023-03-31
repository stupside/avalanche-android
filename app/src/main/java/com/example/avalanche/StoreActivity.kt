@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.shared.AvalancheBottomBar
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.AvalancheHeader
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.StoreViewModel

class StoreActivity : ComponentActivity() {

    private lateinit var stationVm: StoreViewModel

    companion object {
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String): Intent {
            return Intent(context, StoreActivity::class.java).putExtra(StoreIdKey, storeId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!

        stationVm = StoreViewModel(storeId)

        stationVm.loadStore(this)
        stationVm.loadPlans(this)

        setContent {

            val storeState: StoreService.GetStoreProto.Response? by stationVm.store.observeAsState(
                null
            )

            val planStates: List<PlanService.GetPlansProto.Response> by stationVm.plans.collectAsState()

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Store")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        storeState?.let { store ->

                            StoreHeader(
                                name = store.name,
                                description = store.description,
                                logo = store.logo.toString()
                            )

                            Column {

                                Text("Plans", style = MaterialTheme.typography.titleMedium)

                                AvalancheList(elements = planStates) { plan ->
                                    PlanItem(
                                        context = this@StoreActivity,
                                        storeId = storeId,
                                        planId = plan.planId,
                                        name = plan.name,
                                        description = "Plan description"
                                    )
                                }
                            }

                        }
                    }
                }, bottomBar = {
                    AvalancheBottomBar(this, floating = null)
                })
            }
        }
    }
}

@Composable
fun StoreHeader(
    name: String,
    description: String,
    logo: String?
) {
    AvalancheHeader(name, description, logo)
}

@Composable
fun PlanItem(context: Context, storeId: String, planId: String, name: String, description: String) {

    val checkInIntent = PaymentCheckInActivity.getIntent(context, storeId, planId)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(checkInIntent)
        }),
        headlineContent = { Text(name, style = MaterialTheme.typography.titleSmall) },
        supportingContent = { Text(description, style = MaterialTheme.typography.bodyMedium) },
    )
}