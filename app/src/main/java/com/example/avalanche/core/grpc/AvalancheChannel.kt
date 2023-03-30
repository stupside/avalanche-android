package com.example.avalanche.core.grpc

import com.example.avalanche.core.envrionment.Constants
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference

class AvalancheChannel {

    companion object {
        private val INSTANCE_REF =
            AtomicReference<WeakReference<ManagedChannel>>(WeakReference(null))

        fun getInstance(): ManagedChannel {

            var channel: ManagedChannel? = INSTANCE_REF.get().get()

            if (channel == null) {

                channel = ManagedChannelBuilder.forTarget(Constants.AVALANCHE_GATEWAY_GRPC).usePlaintext()
                    .build()

                INSTANCE_REF.set(WeakReference(channel))
            }

            return channel!!
        }
    }
}