package com.example.avalanche.views.stores

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.avalanche.PaymentActivity
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.AvalancheHeader
import com.example.avalanche.core.ui.shared.AvalancheLogo
import com.example.avalanche.core.ui.shared.list.AvalancheList

@Composable
fun StoresView(context: Context, viewModel: StoresViewModel, storeId: String?) {

    var showStoreWithId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(storeId) {
        showStoreWithId = storeId
    }

    LaunchedEffect(showStoreWithId) {
        showStoreWithId?.let {
            viewModel.loadStore(context, it)
        }
    }

    val stores: List<StoreService.GetStoresProto.Response> by viewModel.stores.collectAsState()

    val store: StoreService.GetStoreProto.Response? by viewModel.store.observeAsState()
    val plans: List<PlanService.GetPlansProto.Response> by viewModel.plans.collectAsState()

    Scaffold(topBar = {
        StoresAppBar(context)
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            StoreSearchBar(onType = {
                try {
                    viewModel.loadStores(context, it)
                } catch (_: Exception) {
                }
            }) { close ->
                AvalancheList(stores, template = { store ->
                    ListItem(
                        modifier = Modifier.clickable(onClick = {

                            try {
                                viewModel.loadStore(context, store.storeId)
                                viewModel.loadPlans(context, store.storeId)

                                showStoreWithId = store.storeId
                            } catch (_: Exception) {
                            } finally {
                                close()
                            }
                        }),
                        headlineContent = { Text(store.name) },
                        leadingContent = {
                            AvalancheLogo(store.logo.toString())
                        },
                    )
                })
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (store == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Nothing to show",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                store?.let { it ->
                    Column {
                        StoreHeader(it.name, it.description, it.logo.toString())

                        showStoreWithId?.let { storeId ->
                            AvalancheList(plans) { plan ->
                                PlanItem(
                                    context = context,
                                    storeId = storeId,
                                    planId = plan.planId,
                                    name = plan.name,
                                    description = "Plan description"
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun StoresAppBar(context: Context) {
    TopAppBar(title = {
        Text("Stores")
    }, navigationIcon = {
        AvalancheGoBackButton(context)
    })
}

@Composable
fun StoreSearchBar(
    onType: (name: String) -> Unit,
    content: @Composable (close: () -> Unit) -> Unit
) {
    var name by remember { mutableStateOf("") }

    var searching by rememberSaveable { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .semantics { isContainer = true }
                .zIndex(1f)
                .fillMaxWidth()) {
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                query = name,
                onQueryChange = {
                    name = it
                    onType(it)
                },
                onSearch = {
                    searching = false
                },
                active = searching,
                onActiveChange = {
                    searching = it
                },
                placeholder = { Text("Search a store by name") },
                leadingIcon = {
                    if (searching) {
                        IconButton(onClick = {
                            searching = false
                        }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                content = {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                    ) {
                        content {
                            searching = false
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun StoreHeader(
    name: String,
    description: String,
    logo: String?
) {
    AvalancheHeader(name, description, logo)
}

@Composable
fun PlanItem(
    context: Context,
    storeId: String,
    planId: String,
    name: String,
    description: String
) {
    val checkInIntent =
        PaymentActivity.getIntent(
            context,
            storeId,
            planId
        )

    ListItem(
        headlineContent = { Text(name) },
        supportingContent = { Text(description) },
        trailingContent = {
            IconButton(onClick = {
                context.startActivity(checkInIntent)
            }) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = "Check in $name")
            }
        }
    )
}