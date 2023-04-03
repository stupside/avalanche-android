package com.example.avalanche.core.environment

import android.content.Context
import android.content.SharedPreferences

class Constants {
    companion object {

        val STORE_ID = null
        const val DEVICE_IDENTIFIER = "dev"


        private const val AVALANCHE_NETWORK = "192.168.2.58"

        const val AVALANCHE_IDENTITY = "http://$AVALANCHE_NETWORK:5678"

        const val AVALANCHE_IDENTITY_ACCOUNTS = "$AVALANCHE_IDENTITY/accounts"

        const val AVALANCHE_IDENTITY_CLIENT_ID = "android"
        val AVALANCHE_IDENTITY_SCOPES = listOf("avalanche", "market", "sales", "passport")

        const val AVALANCHE_GATEWAY_GRPC = "$AVALANCHE_NETWORK:5000"

        private const val AVALANCHE_GATEWAY_HTTP = "http://$AVALANCHE_NETWORK:5001"
        private const val AVALANCHE_GATEWAY_HTTPS = "https://$AVALANCHE_NETWORK:5002"

        private const val AVALANCHE_SHARED_PREFERENCES = "Avalanche"

        const val AVALANCHE_SHARED_PREFERENCES_IDENTITY = "$AVALANCHE_SHARED_PREFERENCES.Identity"
        const val AVALANCHE_SHARED_PREFERENCES_STORE = "$AVALANCHE_SHARED_PREFERENCES.Store"

        fun getSharedPreferences(context: Context): SharedPreferences? {
            return context.getSharedPreferences(AVALANCHE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        }
    }
}