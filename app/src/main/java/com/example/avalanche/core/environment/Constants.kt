package com.example.avalanche.core.environment

class Constants {
    companion object {

        private const val AVALANCHE_NETWORK = "192.168.2.58"

        const val AVALANCHE_IDENTITY = "http://$AVALANCHE_NETWORK:5678"

        const val AVALANCHE_IDENTITY_ACCOUNTS = "$AVALANCHE_IDENTITY/accounts"

        const val AVALANCHE_IDENTITY_CLIENT_ID = "android"
        val AVALANCHE_IDENTITY_SCOPES = listOf("avalanche", "market", "sales", "passport", "tracker")


        const val AVALANCHE_GATEWAY_GRPC = "$AVALANCHE_NETWORK:5000"

        private const val AVALANCHE_GATEWAY_HTTP = "http://$AVALANCHE_NETWORK:5001"
        private const val AVALANCHE_GATEWAY_HTTPS = "https://$AVALANCHE_NETWORK:5002"
    }
}