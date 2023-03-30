package com.example.avalanche

import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.avalanche.core.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.nfc.NfcActivity

class TicketNfcActivity : NfcActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheScaffold(activity = this, content = {

            }, button = {})
        }
    }
}