package com.example.avalanche

import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import com.example.avalanche.ui.shared.AvalancheSection
import com.example.avalanche.ui.shared.list.AvalancheList
import com.example.avalanche.ui.shared.list.AvalancheListElement
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold
import com.example.avalanche.vms.StoresViewModel

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

            if (search.isNotEmpty())
                storesVm.loadStores(this, search)

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = "Search") {

                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Name") }
                    )

                    val stores: List<StoreService.GetStoresProto.Response> by storesVm.stations.observeAsState(
                        emptyList()
                    )

                    AvalancheList(elements = stores, template = { store ->

                        if (store.logo != null) {

                            val bytes = store.logo.toByteArray()

                            val bitmap =
                                BitmapFactory.decodeByteArray(bytes, 0, store.logo.serializedSize)

                            if (bitmap == null) {
                                // TODO: DO SOMETHING
                            } else {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = store.name
                                )
                            }
                        }

                        AvalancheListElement(
                            onClick = {
                                val intent = StoreActivity.getIntent(this, store.storeId)

                                startActivity(intent)
                            },
                            content = {

                                Text(store.name)
                                Text(store.description)

                                Text(store.storeId)
                            })
                    })
                }
            }, button = {})
        }
    }
}