package com.example.avalanche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.ui.theme.AvalancheTheme

class PassInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheScaffold(activity = this, content = {

            }, button = {})
        }
    }
}