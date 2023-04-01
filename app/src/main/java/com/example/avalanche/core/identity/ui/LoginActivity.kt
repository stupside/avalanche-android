package com.example.avalanche.core.identity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.WalletsActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.identity.DevelopmentConnectionBuilder
import com.example.avalanche.core.ui.theme.AvalancheTheme
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService

class LoginActivity : ComponentActivity() {

    private val vmLogin: LoginViewModel by viewModels()

    lateinit var service: AuthorizationService

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        service.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = AuthorizationService(
            application.applicationContext, AppAuthConfiguration.Builder()
                .setConnectionBuilder(
                    DevelopmentConnectionBuilder.getInstance()
                ).build()
        )

        val state = AvalancheIdentityState.getInstance(this)

        val registerIntent = RegisterActivity.getIntent(this)
        val walletsIntent = WalletsActivity.getIntent(this)

        setContent {

            var username by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            AvalancheTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Column {
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
                        }

                        Spacer(modifier = Modifier.padding(vertical = 8.dp))

                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedButton(
                                onClick = {

                                    val request = vmLogin.getTokenRequestForPasswordFlow(
                                        state,
                                        username,
                                        password,
                                        Constants.AVALANCHE_IDENTITY_SCOPES
                                    )

                                    service.performTokenRequest(request) { response, exception ->
                                        state.updateAfterTokenResponse(
                                            response = response,
                                            exception = exception
                                        )

                                        if (exception == null) {
                                            startActivity(walletsIntent)
                                        }
                                    }
                                }
                            ) {
                                Text("Login")
                            }
                            TextButton(
                                onClick = {
                                    startActivity(registerIntent)
                                }
                            ) {
                                Text("Register")
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        val state = AvalancheIdentityState.getInstance(this)

        if (state.get().isAuthorized) {
            val walletsActivity = WalletsActivity.getIntent(this)

            startActivity(walletsActivity)
        }

    }
}