package com.example.avalanche.di

import com.example.avalanche.environment.Constants
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module
import java.util.concurrent.Executors

val grpc = module {

    single {

        val builder = ManagedChannelBuilder
            .forTarget(Constants.AVALANCHE_GATEWAY_GRPC)
            .executor(Executors.newSingleThreadExecutor())
            .usePlaintext()

        builder.build()
    }
}