package com.example.avalanche

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

    private val identity = AvalancheIdentityViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                            identity.auth.performTokenRequest(request) { response, exception ->
                                identity.manager.updateAfterTokenResponse(
                                    response = response,
                                    exception = exception
                                )
                            }

                            // identity.manager.get().isAuthorized
                        }
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}