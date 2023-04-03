package com.example.avalanche.core.identity

import android.content.Context
import android.net.Uri
import androidx.annotation.AnyThread
import com.example.avalanche.core.environment.Constants
import net.openid.appauth.*
import org.json.JSONException
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference


class AvalancheIdentityState private constructor(val context: Context) {

    companion object {

        private val INSTANCE_REF =
            AtomicReference<WeakReference<AvalancheIdentityState>>(WeakReference(null))

        @AnyThread
        fun getInstance(context: Context): AvalancheIdentityState {

            var identity: AvalancheIdentityState? = INSTANCE_REF.get().get()

            if (identity == null) {

                identity = AvalancheIdentityState(context.applicationContext)

                INSTANCE_REF.set(WeakReference(identity))
            }

            return identity
        }
    }

    init {
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(Constants.AVALANCHE_IDENTITY),
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { configuration, exception ->
                if (exception == null) {

                    if (configuration == null)
                        return@RetrieveConfigurationCallback

                    update(AuthState(configuration))
                }

                return@RetrieveConfigurationCallback
            }, DevelopmentConnectionBuilder.getInstance()
        )
    }

    @AnyThread
    fun updateAfterAuthorization(
        response: AuthorizationResponse?,
        ex: AuthorizationException?
    ): AuthState {
        val current = readState()

        current.update(response, ex)

        return update(current)
    }

    @AnyThread
    fun updateAfterTokenResponse(
        response: TokenResponse?,
        exception: AuthorizationException?
    ): AuthState {
        val current = readState()

        current.update(response, exception)

        return update(current)
    }

    @AnyThread
    private fun update(state: AuthState): AuthState {

        writeState(state)

        return state
    }

    @AnyThread
    public fun readState(): AuthState {
        val currentState = Constants.getSharedPreferences(context)
            ?.getString(Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY, null)
            ?: return AuthState()

        return try {
            AuthState.jsonDeserialize(currentState)
        } catch (ex: JSONException) {
            AuthState()
        }
    }

    @AnyThread
    private fun writeState(state: AuthState?) {
        val editor = Constants.getSharedPreferences(context)?.edit()

        if (state == null) {
            editor?.remove(Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY)
        } else {
            editor?.putString(
                Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY,
                state.jsonSerializeString()
            )
        }

        editor?.apply()
    }
}