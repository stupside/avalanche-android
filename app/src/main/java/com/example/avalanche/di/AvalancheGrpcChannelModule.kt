package com.example.avalanche.di

import com.example.avalanche.environment.Constants
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module

val grpc = module {

    single {

        val builder = ManagedChannelBuilder
            .forTarget(Constants.AVALANCHE_GATEWAY_GRPC)
            .usePlaintext()

        builder.build()
    }
}