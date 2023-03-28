package com.example.avalanche.identity.ui

import androidx.lifecycle.ViewModel
import com.example.avalanche.identity.AvalancheIdentityState
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenRequest.GRANT_TYPE_PASSWORD


class IdentityActivityViewModel : ViewModel() {

    companion object {
        private const val CLIENT_ID = "android"
    }

    fun getTokenRequestForPasswordFlow(
        state: AvalancheIdentityState,
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