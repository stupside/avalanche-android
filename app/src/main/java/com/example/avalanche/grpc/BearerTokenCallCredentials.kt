package com.example.avalanche.grpc

import io.grpc.*
import java.util.concurrent.Executor

class BearerTokenCallCredentials(private val token: String) : CallCredentials() {

    override fun applyRequestMetadata(
        requestInfo: RequestInfo,
        appExecutor: Executor,
        applier: MetadataApplier
    ) {

        appExecutor.execute {
            try {
                val headers = Metadata()

                headers.put(
                    Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER),
                    "Bearer $token"
                )

                applier.apply(headers)

            } catch (exception: Throwable) {
                applier.fail(Status.UNAUTHENTICATED.withCause(exception))
            }
        }
    }

    override fun thisUsesUnstableApi() {}
}