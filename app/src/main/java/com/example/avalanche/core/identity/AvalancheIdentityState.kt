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

    private var reference: WeakReference<AuthState> = WeakReference<AuthState>(null)

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
    fun readState(): AuthState {

        val auth = reference.get()

        if(auth != null)
        {
            return auth
        }
        else {

            val preference = Constants.getSharedPreferences(context)
                ?.getString(Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY, null)
                ?: return AuthState()

            return try {
                val json = AuthState.jsonDeserialize(preference)

                reference = WeakReference(json)

                json
            } catch (ex: JSONException) {
                AuthState()
            }
        }
    }

    @AnyThread
    private fun writeState(state: AuthState?) {
        val editor = Constants.getSharedPreferences(context)?.edit()

        if (state == null) {
            reference.clear()
            
            editor?.remove(Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY)
        } else {
            val json = state.jsonSerializeString()

            editor?.putString(
                Constants.AVALANCHE_SHARED_PREFERENCES_IDENTITY,json
            )

            reference = WeakReference(state)
        }

        editor?.apply()
    }
}