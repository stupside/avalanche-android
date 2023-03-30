package com.example.avalanche

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.list.AvalancheList
import com.example.avalanche.ui.shared.list.AvalancheListElement
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.StoreViewModel

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

                        if (store.logo != null) {

                            val bytes = store.logo.toByteArray()

                            val bitmap =
                                BitmapFactory.decodeByteArray(bytes, 0, store.logo.serializedSize)

                            if (bitmap == null) {
                                // TODO: show a placeholder
                            } else {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = store.name
                                )
                            }
                        }
                    }
                    AvalancheSection(title = "Passes") {
                        AvalancheList(elements = planStates) { plan ->
                            AvalancheListElement(content = {
                                Text(plan.name)
                                Text(plan.planId)
                            }, onClick = {
                                // TODO: show payment
                            })
                        }
                    }
                }
            }, button = {})
        }
    }
}