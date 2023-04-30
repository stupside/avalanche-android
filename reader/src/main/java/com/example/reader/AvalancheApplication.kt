package com.example.reader

import android.app.Application
import com.example.core.di.authorization
import com.example.core.di.grpc
import com.example.core.di.preferences
import com.example.reader.features.LoginViewModel
import com.example.reader.features.RegisterViewModel
import com.example.reader.features.store.StoreViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val vms = module {

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        StoreViewModel(get(), get())
    }
}

class AvalancheApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidLogger()

            androidContext(this@AvalancheApplication)

            modules(preferences, authorization, grpc, vms)
        }
    }
}