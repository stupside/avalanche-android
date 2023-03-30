@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.shared.AvalancheSection
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.shared.scaffold.AvalancheScaffold
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

            val planStates: List<PlanService.GetPlansProto.Response> by stationVm.plans.observeAsState(
                emptyList()
            )

            AvalancheScaffold(activity = this, content = {

                storeState?.let { store ->
                    AvalancheSection(store.name) {
                        Text(store.description)
                        Text(store.email)
                    }

                    AvalancheSection(title = "Plans") {
                        AvalancheList(elements = planStates) { plan ->
                            PlanItem(context = this, name = plan.name, description = "Plan description")
                        }
                    }
                }
            }, button = {})
        }
    }
}

@Composable
fun StoreHeader(context: Context, store: String, name: String, description: String, logo: String?){
    ElevatedCard {
        Box(Modifier.fillMaxWidth()) {
            Text(store)
            Text(name)
            Text(description)
            StoreLogo(logo)
        }
    }
}

@Composable
fun PlanItem(context: Context, name: String, description: String){

    ListItem(
        headlineText = { Text(name) },
        supportingText = { Text(description) },
        trailingContent = {
            Button(onClick = {
                // TODO: show plans
            }) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = description
                )
            }
        }
    )
}