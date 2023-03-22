package com.example.avalanche.identity

import android.content.Context
import android.net.Uri
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenRequest.GRANT_TYPE_PASSWORD


class AvalancheIdentityViewModel(context: Context) {

    var service: AuthorizationService = AuthorizationService(
        context, AppAuthConfiguration.Builder().setConnectionBuilder(
            DevelopmentConnectionBuilder.getInstance()
        ).build()
    )

    val state: AvalancheIdentityState = AvalancheIdentityState(context)

    companion object {

        private const val CLIENT_ID = "android"

        private const val IDENTITY_SERVER_URI = "https://192.168.2.58:8180"
    }

    init {
        AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse(IDENTITY_SERVER_URI),
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { configuration, exception ->
                if (exception == null) {

                    if (configuration == null)
                        return@RetrieveConfigurationCallback

                    state.replace(AuthState(configuration))
                }

                return@RetrieveConfigurationCallback
            }, DevelopmentConnectionBuilder.getInstance())
    }

    fun getTokenRequestForPasswordFlow(
        username: String,
        password: String,
        scopes: Iterable<String>
    ): TokenRequest {

        val auth = state.get()

        val configuration = auth.authorizationServiceConfiguration!!

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