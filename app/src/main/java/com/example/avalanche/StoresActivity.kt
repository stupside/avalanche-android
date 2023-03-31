@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

            var search by remember { mutableStateOf("") }

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Stores")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            label = { Text("Name") }
                        )
                        
                        TextButton(onClick = {
                            storesVm.loadStores(this@StoresActivity, search)
                        }) {
                            Text("Search")
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
        headlineText = { Text(name) },
        leadingContent = {
            StoreLogo(logo)
        },
        supportingText = { Text(description) },
    )
}