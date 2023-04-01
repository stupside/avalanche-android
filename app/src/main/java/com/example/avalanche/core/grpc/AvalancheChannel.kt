package com.example.avalanche.core.grpc

import com.example.avalanche.core.environment.Constants
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

class AvalancheChannel {

    companion object {
        fun getNew(): ManagedChannel {
            val builder = ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext()
            return try {
                builder.build()
            } catch (e: java.lang.RuntimeException){
                builder.build()
            }
        }
    }
}