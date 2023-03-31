package com.example.avalanche.core.grpc

import com.example.avalanche.core.envrionment.Constants
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

class AvalancheChannel {

    companion object {
        fun getNew(): ManagedChannel {
            return ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext()
                .build()
        }
    }
}