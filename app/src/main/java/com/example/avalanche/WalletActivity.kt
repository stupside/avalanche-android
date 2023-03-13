package com.example.avalanche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
//import com.example.avalanche.layouts.ListLayout
import com.example.avalanche.ui.theme.AvalancheTheme


class WalletActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheTheme {
                val activityTitle = "Wallet";
                val floatingButton = true
                //val listLayoutTest = ListLayout(activityTitle, floatingButton)
                //listLayoutTest.ScaffoldBase(modifier = Modifier.fillMaxSize(), activityTitle = activityTitle)
            }
        }
    }
}

