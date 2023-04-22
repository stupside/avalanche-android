package com.example.avalanche.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avalanche.di.services.AvalancheIdentityService
import kotlinx.coroutines.launch

class LoginViewModel constructor(private val identity: AvalancheIdentityService) :
    ViewModel() {

    fun login(username: String, password: String, onLogin: () -> Unit) {

        viewModelScope.launch {
            identity.login(username, password, this, onLogin) {}
        }
    }
}