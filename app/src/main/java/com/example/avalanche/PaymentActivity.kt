package com.example.avalanche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheScaffold(activity = this, content = {

            }, button = {})
        }
    }
}