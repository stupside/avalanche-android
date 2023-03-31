@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.zIndex
import com.example.avalanche.core.ui.shared.AvalancheBottomBar
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.StoresViewModel

class StoresActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, StoresActivity::class.java)
        }
    }

    private lateinit var storesVm: StoresViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storesVm = StoresViewModel()

        setContent {

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Stores")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        var nameSearch by remember { mutableStateOf("") }
                        var active by rememberSaveable { mutableStateOf(false) }

                        Box(
                            Modifier
                                .semantics { isContainer = true }
                                .zIndex(1f)
                                .fillMaxWidth()) {
                            SearchBar(
                                modifier = Modifier.align(Alignment.TopCenter),
                                query = nameSearch,
                                onQueryChange = { nameSearch = it },
                                onSearch = {
                                    active = false

                                    storesVm.loadStores(this@StoresActivity, nameSearch)
                                },
                                active = active,
                                onActiveChange = {
                                    // active = it
                                },
                                placeholder = { Text("Hinted search text") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null
                                    )
                                },
                            ) {
                            }
                        }

                        val stores: List<StoreService.GetStoresProto.Response> by storesVm.stores.collectAsState()

                        AvalancheList(elements = stores, template = { store ->
                            StoreItem(
                                context = this@StoresActivity,
                                store = store.storeId,
                                name = store.name,
                                description = store.description,
                                logo = store.logo.toString()
                            )
                        })
                    }
                }, bottomBar = {
                    AvalancheBottomBar(this, floating = null)
                })
            }
        }
    }
}

@Composable
fun StoreItem(
    context: Context,
    store: String,
    name: String,
    description: String,
    logo: String?
) {
    val intent = StoreActivity.getIntent(context, store)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(intent)
        }),
        headlineContent = { Text(name) },
        leadingContent = {
            StoreLogo(logo)
        },
        supportingContent = { Text(description) },
    )
}