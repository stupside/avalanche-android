package com.example.avalanche.views.challenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.ExperimentalGetImage
import com.example.avalanche.core.ui.theme.AvalancheTheme

@ExperimentalGetImage
class DiscoveryActivity: ComponentActivity(){

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DiscoveryActivity::class.java)
        }
    }

    private val discoveryVm: DiscoveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AvalancheTheme {
                DiscoveryView(context = this, viewModel = discoveryVm)
            }
        }
    }
}