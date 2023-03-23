package com.example.avalanche

import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.StationViewModel


class StationActivity : ComponentActivity() {

    private val vm : StationViewModel by viewModels()

    companion object {
        private const val StationIdKey = "StationId"

        fun getIntent(context: Context, stationId: String): Intent {
            return Intent(context, StationActivity::class.java).putExtra(StationIdKey, stationId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stationId = intent.getStringExtra(StationIdKey)!!

        vm.load(this, stationId)

        setContent {

            val store: StoreService.GetStoreProto.Response? by vm.data.observeAsState(null)

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = store?.name.toString(), content = {

                    Text(text = store?.description.toString())
                    Text(text = store?.email.toString())
                    Text(text = store?.logo.toString())

                })
            }, button = {})
        }
    }
}