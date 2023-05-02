package com.example.avalanche

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.avalanche.ui.drm.ReaderActivity
import com.example.avalanche.ui.features.LoginView
import com.example.avalanche.ui.features.RegisterView
import com.example.avalanche.ui.features.order.OrderView
import com.example.avalanche.ui.features.store.StoreView
import com.example.avalanche.ui.features.stores.StoresView
import com.example.avalanche.ui.features.ticket.TicketView
import com.example.avalanche.ui.features.wallet.WalletView
import org.koin.androidx.compose.koinViewModel

sealed class AvalancheNavHostLink(val route: String) {

    object Login : AvalancheNavHostLink("login")
    object Register : AvalancheNavHostLink("register")

    object Stores : AvalancheNavHostLink("stores")

    object Store : AvalancheNavHostLink("stores/{storeId}") {
        fun route(storeId: String) = "stores/$storeId"
    }

    object Wallet : AvalancheNavHostLink("wallet")

    object Ticket : AvalancheNavHostLink("wallet/{ticketId}") {
        fun route(ticketId: String) = "wallet/$ticketId"
    }

    object Order : AvalancheNavHostLink("plan/{planId}/order") {
        fun route(planId: String) = "plan/$planId/order"
    }
}

@Composable
fun AvalancheNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AvalancheNavHostLink.Login.route) {

        composable(AvalancheNavHostLink.Login.route) {

            LoginView(viewModel = koinViewModel(), onLogin = {
                navController.navigate(AvalancheNavHostLink.Wallet.route)
            }, goRegister = {
                navController.navigate(AvalancheNavHostLink.Register.route)
            })
        }

        composable(AvalancheNavHostLink.Register.route) {

            RegisterView(viewModel = koinViewModel(), onRegister = {
                navController.navigate(AvalancheNavHostLink.Login.route)
            })
        }

        composable(
            route = AvalancheNavHostLink.Order.route,
            arguments = listOf(navArgument("planId") {
                type = NavType.StringType
            })
        ) {

            val planId = it.arguments?.getString("planId")!!

            OrderView(viewModel = koinViewModel(), planId = planId, goBack = {
                navController.popBackStack()
            })
        }

        composable(AvalancheNavHostLink.Stores.route) {

            StoresView(viewModel = koinViewModel(), goBack = {
                navController.popBackStack()
            }, goStore = { storeId ->
                navController.navigate(AvalancheNavHostLink.Store.route(storeId))
            })
        }

        composable(
            route = AvalancheNavHostLink.Store.route,
            arguments = listOf(navArgument("storeId") {
                type = NavType.StringType
            })
        ) {

            val storeId = it.arguments?.getString("storeId")!!

            StoreView(viewModel = koinViewModel(), storeId = storeId, goBack = {
                navController.popBackStack()
            }, goOrder = { planId ->
                navController.navigate(AvalancheNavHostLink.Order.route(planId))
            }, goDrmReader = {

                val intent = Intent(navController.context, ReaderActivity::class.java).apply {
                    putExtra(ReaderActivity.INTENT_EXTRA_STORE_ID, storeId)
                }

                navController.context.startActivity(intent)
            })
        }

        composable(
            route = AvalancheNavHostLink.Ticket.route,
            arguments = listOf(navArgument("ticketId") {
                type = NavType.StringType
            })
        ) {

            val ticketId = it.arguments?.getString("ticketId")!!

            TicketView(viewModel = koinViewModel(), ticketId = ticketId, goBack = {
                navController.popBackStack()
            }, goStore = { storeId ->
                navController.navigate(AvalancheNavHostLink.Store.route(storeId))
            })
        }

        composable(AvalancheNavHostLink.Wallet.route) {

            WalletView(viewModel = koinViewModel(), goStores = {
                navController.navigate(AvalancheNavHostLink.Stores.route)
            }, goTicket = { ticketId ->
                navController.navigate(AvalancheNavHostLink.Ticket.route(ticketId))
            })
        }
    }
}