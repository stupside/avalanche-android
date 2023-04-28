package com.example.avalanche.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.services.AvalancheIdentityService

class LoginViewModel constructor(private val identity: AvalancheIdentityService) :
    ViewModel() {

    fun login(username: String, password: String, onLogin: () -> Unit) {
        identity.login(username, password, viewModelScope, onLogin) {}
    }
}