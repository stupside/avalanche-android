package com.example.avalanche

import android.app.Application
import com.example.avalanche.ui.drm.ReaderViewModel
import com.example.avalanche.ui.drm.WriterViewModel
import com.example.avalanche.ui.features.LoginViewModel
import com.example.avalanche.ui.features.RegisterViewModel
import com.example.avalanche.ui.features.order.OrderViewModel
import com.example.avalanche.ui.features.store.StoreViewModel
import com.example.avalanche.ui.features.stores.StoresViewModel
import com.example.avalanche.ui.features.ticket.TicketViewModel
import com.example.avalanche.ui.features.wallet.WalletViewModel
import com.example.core.di.authorization
import com.example.core.di.grpc
import com.example.core.di.preferences
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

    viewModel {
        StoreViewModel(get(), get())
    }

    viewModel {
        OrderViewModel(get(), get())
    }

    viewModel {
        ReaderViewModel(get(), get())
    }
    viewModel {
        WriterViewModel(get(), get())
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