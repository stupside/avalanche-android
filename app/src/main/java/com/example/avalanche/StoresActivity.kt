@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import com.example.avalanche.core.ui.shared.AvalancheSection
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.viewmodels.StoresViewModel

class StoresActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, StoresActivity::class.java)
        }
    }

    private val storesVm: StoresViewModel by lazy {
        StoresViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var search by remember { mutableStateOf("") }


            storesVm.loadStores(this, search)


            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = "Search") {

                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Name") }
                    )

                    val stores: List<StoreService.GetStoresProto.Response> by storesVm.stores.collectAsState()

                    AvalancheList(elements = stores, template = { store ->
                        StoreItem(
                            context = this,
                            store = store.storeId,
                            name = store.name,
                            description = store.description,
                            logo = store.logo.toString()
                        )
                    })
                }
            }, button = {})
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
        headlineText = { Text(name) },
        leadingContent = {
            StoreLogo(logo)
        },
        trailingContent = {
            Button(onClick = {
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = description
                )
            }
        },
        supportingText = { Text(description) }
    )
}

@Composable
fun StoreLogo(logo: String?) {
    if (logo == null) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = "Placeholder"
        )
    } else {

        val bytes = logo.toByteArray()

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, logo.length)

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = logo
        )
    }
}