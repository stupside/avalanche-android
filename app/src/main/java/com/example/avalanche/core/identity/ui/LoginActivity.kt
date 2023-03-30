package com.example.avalanche.core.identity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.avalanche.StoresActivity
import com.example.avalanche.WalletsActivity
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.identity.DevelopmentConnectionBuilder
import com.example.avalanche.core.envrionment.Constants
import com.example.avalanche.core.ui.shared.scaffold.AvalancheScaffold
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

        val register = RegisterActivity.getIntent(this)

        setContent {

            var username by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            val walletsIntent = WalletsActivity.getIntent(this)

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
                    Button(
                        onClick = {
                            startActivity(register)
                        }
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val state = AvalancheIdentityState.getInstance(this)

        if (state.get().isAuthorized) {
            val stationsIntent = Intent(this, StoresActivity::class.java)

            startActivity(stationsIntent)
        }

    }
}