package com.example.avalanche.layouts


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.avalanche.LoginActivity
import com.example.avalanche.WalletActivity
import com.example.avalanche.ui.theme.AvalancheTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginLayout() {
    var usernametext by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Enter Username", TextRange(0, 7)))
    }
    var passwordtext by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Enter Password", TextRange(0, 7)))
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = usernametext,
                onValueChange = { usernametext = it },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = passwordtext,
                onValueChange = { passwordtext = it },
                label = { Text("Password") }
            )
            Button(
                onClick = {
                    /*Todo: Ajouter la logique suivante
                    * Doit faire l'intent vers wallet après avoir
                    * vérifié que le username et le mot de passe
                    * sont bien en base de donné; sinon affiche
                    * du texte d'erreur en rouge. On laisse un
                    * nombre de chance illimité à l'Utilisateur
                    * d'entrer ses credentials pour l'instant
                    * */
                }
            ) {
                Text("Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreviewListLayout() {
    AvalancheTheme {
        LoginLayout()
    }
}