package com.example.avalanche.core.identity.ui

import androidx.lifecycle.ViewModel
import com.example.avalanche.core.identity.AvalancheIdentityState
import com.example.avalanche.core.environment.Constants
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenRequest.GRANT_TYPE_PASSWORD


class LoginViewModel : ViewModel() {

    fun getTokenRequestForPasswordFlow(
        state: AvalancheIdentityState,
        username: String,
        password: String,
        scopes: Iterable<String>
    ): TokenRequest {

        val auth = state.get()

        val configuration = auth.authorizationServiceConfiguration!!

        val request = TokenRequest.Builder(configuration, Constants.AVALANCHE_IDENTITY_CLIENT_ID)

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