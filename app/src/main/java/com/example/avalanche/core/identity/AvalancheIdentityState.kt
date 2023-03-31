package com.example.avalanche.core.identity

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.annotation.AnyThread
import com.example.avalanche.core.envrionment.Constants
import net.openid.appauth.*
import org.json.JSONException
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference


class AvalancheIdentityState private constructor(context: Context) {

    companion object {

        private const val KEY_AUTH_STATE = "state"

        private const val STORE_NAME = "AuthState"

        private val INSTANCE_REF =
            AtomicReference<WeakReference<AvalancheIdentityState>>(WeakReference(null))

        @AnyThread
        fun getInstance(context: Context): AvalancheIdentityState {

            var manager: AvalancheIdentityState? = INSTANCE_REF.get().get()

            if (manager == null) {
                manager = AvalancheIdentityState(context.applicationContext)
                INSTANCE_REF.set(WeakReference(manager))
            }

            return manager
        }
    }

    private val _prefs: SharedPreferences
    private val _state: AtomicReference<AuthState>

    init {
        _prefs = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

        _state = AtomicReference<AuthState>()

        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(Constants.AVALANCHE_IDENTITY),
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { configuration, exception ->
                if (exception == null) {

                    if (configuration == null)
                        return@RetrieveConfigurationCallback

                    replace(AuthState(configuration))
                }

                return@RetrieveConfigurationCallback
            }, DevelopmentConnectionBuilder.getInstance()
        )
    }

    @AnyThread
    fun get(): AuthState {

        var state = _state.get()

        if (state != null) {
            return state
        }

        state = readState()

        return if (_state.compareAndSet(null, state)) {
            state
        } else {
            _state.get()
        }
    }

    @AnyThread
    fun replace(state: AuthState): AuthState {
        writeState(state)

        _state.set(state)

        return state
    }

    @AnyThread
    fun updateAfterAuthorization(
        response: AuthorizationResponse?,
        ex: AuthorizationException?
    ): AuthState {
        val current = get()

        current.update(response, ex)

        return replace(current)
    }

    @AnyThread
    fun updateAfterTokenResponse(
        response: TokenResponse?,
        exception: AuthorizationException?
    ): AuthState {
        val current = get()

        current.update(response, exception)

        return replace(current)
    }

    @AnyThread
    private fun readState(): AuthState {
        val currentState = _prefs.getString(KEY_AUTH_STATE, null)
            ?: return AuthState()

        return try {
            AuthState.jsonDeserialize(currentState)
        } catch (ex: JSONException) {
            AuthState()
        }
    }

    @AnyThread
    private fun writeState(state: AuthState?) {
        val editor = _prefs.edit()

        if (state == null) {
            editor.remove(KEY_AUTH_STATE)
        } else {
            editor.putString(KEY_AUTH_STATE, state.jsonSerializeString())
        }

        check(editor.commit()) { "Failed to write state to shared prefs" }
    }
}