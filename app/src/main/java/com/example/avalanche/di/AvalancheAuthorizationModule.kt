package com.example.avalanche.di

import com.example.avalanche.core.grpc.BearerTokenCallCredentials
import com.example.avalanche.core.identity.DevelopmentConnectionBuilder
import com.example.avalanche.di.services.AvalancheIdentityService
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authorization = module {

    single {
        AuthorizationService(
            androidContext(), AppAuthConfiguration.Builder().setConnectionBuilder(
                    DevelopmentConnectionBuilder.getInstance()
                ).build()
        )
    }

    single {
        AvalancheIdentityService(get(), get())
    }

    single {

        val identity = get<AvalancheIdentityService>()

        val token = identity.token()

        BearerTokenCallCredentials(token.toString())
    }
}