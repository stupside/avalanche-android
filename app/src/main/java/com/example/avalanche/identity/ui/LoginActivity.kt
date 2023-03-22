package com.example.avalanche.identity.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.avalanche.identity.AvalancheIdentityViewModel
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val identity = AvalancheIdentityViewModel(this)

        setContent {

            var username by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            AvalancheScaffold(activity = this, button = {}) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") }
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") }
                    )
                    Button(
                        onClick = {

                            val request = identity.getTokenRequestForPasswordFlow(
                                username,
                                password,
                                listOf("market", "passport", "tracker")
                            )

                            identity.service.performTokenRequest(request) { response, exception ->
                                identity.state.updateAfterTokenResponse(
                                    response = response,
                                    exception = exception
                                )
                            }
                        }
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}