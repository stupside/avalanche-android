package com.example.reader

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reader.features.LoginView
import com.example.reader.features.RegisterView
import com.example.reader.features.store.StoreView
import org.koin.androidx.compose.koinViewModel

sealed class AvalancheNavHostLink(val route: String) {

    object Login : AvalancheNavHostLink("login")
    object Register : AvalancheNavHostLink("register")

    object Store : AvalancheNavHostLink("stores")
}

@Composable
fun AvalancheNavHost(storeId: String) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AvalancheNavHostLink.Login.route) {

        composable(AvalancheNavHostLink.Login.route) {

            LoginView(viewModel = koinViewModel(), onLogin = {
                navController.navigate(AvalancheNavHostLink.Store.route)
            }, goRegister = {
                navController.navigate(AvalancheNavHostLink.Register.route)
            })
        }

        composable(AvalancheNavHostLink.Register.route) {

            RegisterView(viewModel = koinViewModel(), onRegister = {
                navController.navigate(AvalancheNavHostLink.Login.route)
            })
        }

        composable(route = AvalancheNavHostLink.Store.route) {
            StoreView(viewModel = koinViewModel(), storeId = storeId) {
                navController.popBackStack()
            }
        }
    }
}