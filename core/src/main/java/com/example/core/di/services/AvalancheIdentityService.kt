package com.example.core.di.services

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.environment.Constants
import com.example.core.environment.Constants.Companion.AVALANCHE_IDENTITY_SCOPES
import com.example.core.identity.DevelopmentConnectionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException

class AvalancheIdentityService constructor(
    private val store: DataStore<androidx.datastore.preferences.core.Preferences>,
    private val authorization: AuthorizationService
) {

    private lateinit var state: AuthState

    companion object {

        private val IDENTITY = stringPreferencesKey("identity")
    }

    init {

        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(Constants.AVALANCHE_IDENTITY),
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { configuration, _ ->

                if (configuration == null)
                    return@RetrieveConfigurationCallback

                state = AuthState(configuration)

                return@RetrieveConfigurationCallback
            }, DevelopmentConnectionBuilder.getInstance()
        )
    }

    fun token(): String? {

        val data = store.data

        return runBlocking {

            data.firstOrNull()?.get(IDENTITY)
        }
    }

    fun login(
        username: String,
        password: String,
        scope: CoroutineScope,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

        val configuration = state.authorizationServiceConfiguration!!

        val request = TokenRequest.Builder(configuration, Constants.AVALANCHE_IDENTITY_CLIENT_ID)

        request.setGrantType(TokenRequest.GRANT_TYPE_PASSWORD)

        request.setAdditionalParameters(
            mapOf(
                Pair("username", username),
                Pair("password", password)
            )
        )

        request.setScopes(AVALANCHE_IDENTITY_SCOPES)

        authorization.performTokenRequest(request.build()) { response, exception ->

            state.update(response, exception)

            scope.launch {

                store.edit { preferences ->

                    preferences[IDENTITY] = state.accessToken ?: ""
                }

                if (exception == null) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
        }
    }

    fun register(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()

        request.url("${Constants.AVALANCHE_IDENTITY_ACCOUNTS}?username=$username&password=$password")

        request.post(EMPTY_REQUEST)

        client.newCall(request.build()).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}