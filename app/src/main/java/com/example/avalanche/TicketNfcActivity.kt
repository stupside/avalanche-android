package com.example.avalanche

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.nfc.NfcActivity

class TicketNfcActivity : NfcActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Active ticket")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                    }
                })
            }
        }
    }
}