package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.views.wallet.WalletView
import com.example.avalanche.views.wallet.WalletViewModel


class WalletActivity : ComponentActivity() {

    companion object {
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String): Intent {
            return Intent(context, WalletActivity::class.java).putExtra(StoreIdKey, storeId)
        }
    }

    private val walletVm: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!

        setContent {
            AvalancheTheme {
                WalletView(context = this, viewModel = walletVm, storeId = storeId)
            }
        }
    }
}
