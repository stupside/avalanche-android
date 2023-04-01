package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdIntentKey)

        WindowCompat.setDecorFitsSystemWindows(window, false)

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
}