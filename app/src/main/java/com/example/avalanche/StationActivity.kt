package com.example.avalanche

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.list.AvalancheList
import com.example.avalanche.ui.shared.list.AvalancheListElement
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.StationViewModel

//Ac
class StationActivity : ComponentActivity() {

    private val vm: StationViewModel by viewModels()

    companion object {
        private const val StationIdKey = "StationId"

        fun getIntent(context: Context, stationId: String): Intent {
            return Intent(context, StationActivity::class.java).putExtra(StationIdKey, stationId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val state = AvalancheIdentityState.getInstance(this)

        val onBackPressedCallback = object: OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                // Your business logic to handle the back pressed event
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stationId = intent.getStringExtra(StationIdKey)!!

        vm.loadStore(this, stationId)

        vm.loadPlans(this, stationId)

        setContent {

            val store: StoreService.GetStoreProto.Response? by vm.store.observeAsState(null)

            val plans: List<PlanService.GetPlansProto.Response> by vm.plans.observeAsState(
                emptyList()
            )

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = store?.name.toString()) {
                    Text(text = store?.description.toString())
                    Text(text = store?.email.toString())
                    Text(text = store?.logo.toString())
                }
                AvalancheSection(title = "Passes") {
                    AvalancheList(elements = plans) { plan ->
                        AvalancheListElement(content = {
                            Text(plan.name)
                            Text(plan.planId)
                        }) {

                        }
                    }
                }
            }, button = {})
        }
    }
}