package com.example.avalanche.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.di.services.AvalancheIdentityService
import kotlinx.coroutines.launch

class RegisterViewModel constructor(private val identity: AvalancheIdentityService) :
    ViewModel() {

    fun register(username: String, password: String, onRegister: () -> Unit) {
        viewModelScope.launch {
            identity.register(username, password, onRegister) {}
        }
    }
}