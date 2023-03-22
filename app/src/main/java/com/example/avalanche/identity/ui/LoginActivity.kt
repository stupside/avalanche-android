package com.example.avalanche.identity.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.avalanche.identity.AvalancheIdentityState
import com.example.avalanche.identity.AvalancheIdentityViewModel
import com.example.avalanche.identity.DevelopmentConnectionBuilder
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService

class LoginActivity : ComponentActivity() {

    private val identity: AvalancheIdentityViewModel by viewModels()

    lateinit var service: AuthorizationService

    override fun onDestroy() {
        super.onDestroy()

        service.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = AuthorizationService(
            application.applicationContext, AppAuthConfiguration.Builder().setConnectionBuilder(
                DevelopmentConnectionBuilder.getInstance()
            ).build()
        )

        val state = AvalancheIdentityState.getInstance(this)

        setContent {

            var username by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            val intent = Intent(this, IdentityActivity::class.java)

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
                                state,
                                username,
                                password,
                                listOf("market", "passport", "tracker")
                            )

                            service.performTokenRequest(request) { response, exception ->
                                state.updateAfterTokenResponse(
                                    response = response,
                                    exception = exception
                                )

                                if (exception == null) {
                                    startActivity(intent)
                                }
                            }
                        }
                    ) {
                        Text("Login")
                    }
                    Button(
                        onClick = {
                            startActivity(intent)
                        }
                    ) {
                        Text("Show identity")
                    }
                }
            }
        }
    }
}