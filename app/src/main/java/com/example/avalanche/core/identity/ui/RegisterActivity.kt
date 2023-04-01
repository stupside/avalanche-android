package com.example.avalanche.core.identity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.theme.AvalancheTheme
import okhttp3.*
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException


class RegisterActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginIntent = LoginActivity.getIntent(this)

        setContent {

            var username by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var validation by remember {
                mutableStateOf("")
            }

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text("Register")
                        },
                        navigationIcon = {
                            AvalancheGoBackButton(this)
                        })
                }, content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") }
                        )

                        Spacer(modifier = Modifier.padding(vertical = 3.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") }
                        )

                        OutlinedTextField(
                            value = validation,
                            onValueChange = { validation = it },
                            label = { Text("Password validation") }
                        )

                        Spacer(modifier = Modifier.padding(vertical = 8.dp))

                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedButton(
                                enabled = password == validation,
                                onClick = {
                                    val client = OkHttpClient()

                                    val request = Request.Builder()

                                    request.url("${Constants.AVALANCHE_IDENTITY_ACCOUNTS}?username=$username&password=$password")

                                    request.post(EMPTY_REQUEST)

                                    client.newCall(request.build()).enqueue(object : Callback {

                                        override fun onResponse(call: Call, response: Response) {
                                            if (response.isSuccessful) {
                                                startActivity(loginIntent)
                                            }
                                        }

                                        override fun onFailure(call: Call, e: IOException) {
                                        }
                                    })
                                }
                            ) {
                                Text("Register")
                            }
                        }
                    }
                })

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