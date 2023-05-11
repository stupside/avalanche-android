package com.example.core.environment

class Constants {
    companion object {

        private const val AVALANCHE_NETWORK = "192.168.2.58"

        const val TEST_USERNAME = ""
        const val TEST_PASSWORD = ""

        const val AVALANCHE_IDENTITY = "http://$AVALANCHE_NETWORK:5678"

        const val AVALANCHE_IDENTITY_ACCOUNTS = "$AVALANCHE_IDENTITY/accounts"

        const val AVALANCHE_IDENTITY_CLIENT_ID = "android"
        val AVALANCHE_IDENTITY_SCOPES = listOf("avalanche", "merchant", "vault", "drm")

        const val AVALANCHE_GATEWAY_GRPC = "$AVALANCHE_NETWORK:5000"

        private const val AVALANCHE_GATEWAY_HTTP = "http://$AVALANCHE_NETWORK:5001"
        private const val AVALANCHE_GATEWAY_HTTPS = "https://$AVALANCHE_NETWORK:5002"

        val SELECT_APDU = byteArrayOf(
            0x00.toByte(),
            0xA4.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            0x07.toByte(),

            0xF0.toByte(), // AID apduservice.xml
            0x39.toByte(),
            0xE2.toByte(),
            0xD3.toByte(),
            0xC4.toByte(),
            0xB5.toByte(),
            0x00.toByte(),

            0x00.toByte()
        )
    }
}