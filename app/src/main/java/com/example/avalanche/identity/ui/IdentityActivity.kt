package com.example.avalanche.identity.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold

class IdentityActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = AvalancheIdentityState.getInstance(this).get()

        setContent {

            AvalancheScaffold(activity = this, button = {}) {
                Column {
                    Text("state.accessToken: ${state.accessToken}")
                    Text("state.idToken: ${state.idToken}")
                    Text("state.refreshToken: ${state.refreshToken}")
                    Text("state.scope: ${state.scope}")
                    Text("state.accessTokenExpirationTime: ${state.accessTokenExpirationTime}")
                    Text("state.isAuthorized: ${state.isAuthorized}")
                }
            }
        }
    }
}