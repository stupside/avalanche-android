package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.views.wallets.WalletsView
import com.example.avalanche.views.wallets.WalletsViewModel

//
class WalletsActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WalletsActivity::class.java)
        }
    }

    private val walletsVm: WalletsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AvalancheTheme {
                WalletsView(context = this, viewModel = walletsVm)
            }
        }
    }
}