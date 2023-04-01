package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.views.stores.StoresView
import com.example.avalanche.views.stores.StoresViewModel

class StoresActivity : ComponentActivity() {

    companion object {

        private const val StoreIdIntentKey = "StoreIdIntentKey"

        fun getIntent(context: Context, storeId: String? = null): Intent {
            return Intent(context, StoresActivity::class.java).putExtra(StoreIdIntentKey, storeId)
        }
    }

    private val storesVm: StoresViewModel by viewModels()

    var storeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storeId = intent.getStringExtra(StoreIdIntentKey)

        setContent {

            AvalancheTheme {
                StoresView(
                    context = this,
                    viewModel = storesVm,
                    storeId = storeId
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        storeId?.let {
            storesVm.loadStore(this, it)
            storesVm.loadPlans(this, it)
        }
    }
}