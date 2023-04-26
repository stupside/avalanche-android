package com.example.avalanche.ui.features

import androidx.lifecycle.ViewModel
import com.example.avalanche.di.services.AvalancheIdentityService

class RegisterViewModel constructor(private val identity: AvalancheIdentityService) :
    ViewModel() {

    fun register(username: String, password: String, onRegister: () -> Unit) {
        identity.register(username, password, onRegister) {}
    }
}