package com.example.reader.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterView(viewModel: RegisterViewModel, onRegister: () -> Unit) {

    var username by remember {
        mutableStateOf<String?>(null)
    }

    var password by remember {
        mutableStateOf<String?>(null)
    }

    var validation by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(content = { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    modifier = Modifier.weight(2f),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        OutlinedTextField(value = username ?: "",
                            onValueChange = { username = it },
                            label = { Text("Username") })

                        TextField(
                            value = password ?: "",
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )

                        TextField(
                            value = validation ?: "",
                            onValueChange = { validation = it },
                            label = { Text("Password validation") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                }

                Row(
                    modifier = Modifier.weight(3f), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(enabled = !password.isNullOrEmpty() && password == validation,
                        onClick = {
                            viewModel.register(username!!, password!!, onRegister)
                        }) {
                        Text("Register")
                    }
                }
            }
        }
    })
}