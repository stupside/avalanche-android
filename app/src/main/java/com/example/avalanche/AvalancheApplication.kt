package com.example.avalanche

import android.app.Application
import com.example.avalanche.di.authorization
import com.example.avalanche.di.grpc
import com.example.avalanche.di.preferences
import com.example.avalanche.ui.features.LoginViewModel
import com.example.avalanche.ui.features.RegisterViewModel
import com.example.avalanche.ui.features.stores.StoresViewModel
import com.example.avalanche.ui.features.ticket.TicketViewModel
import com.example.avalanche.ui.features.wallet.WalletViewModel
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
        WalletViewModel(get(), get())
    }

    viewModel {
        TicketViewModel(get(), get())
    }

    viewModel {
        StoresViewModel(get(), get())
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