package com.example.avalanche.identity

import android.content.Context
import android.net.Uri
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenRequest.GRANT_TYPE_PASSWORD


class AvalancheIdentityViewModel(context: Context) {

    var auth: AuthorizationService = AuthorizationService(context)

    val manager: AvalancheIdentityManager = AvalancheIdentityManager(context)

    companion object {

        private val TAG = AvalancheIdentityViewModel::class.simpleName

        private const val CLIENT_ID = "android"

        private const val IDENTITY_SERVER_URI = "https://localhost:8180"
    }

    init {
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(IDENTITY_SERVER_URI),
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { configuration, exception ->
                if (exception == null) {

                    if (configuration == null)
                        return@RetrieveConfigurationCallback

                    manager.replace(AuthState(configuration))
                }

                return@RetrieveConfigurationCallback
            })
    }

    fun getTokenRequestForPasswordFlow(
        username: String,
        password: String,
        scopes: Iterable<String>
    ): TokenRequest {

        val configuration = manager.get().authorizationServiceConfiguration!!

        val request = TokenRequest.Builder(configuration, CLIENT_ID)

        request.setGrantType(GRANT_TYPE_PASSWORD)

        request.setAdditionalParameters(
            mapOf(
                Pair("username", username),
                Pair("password", password)
            )
        )

        request.setScopes(scopes)

        return request.build()
    }
}