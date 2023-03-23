package com.example.avalanche

import Avalanche.Market.StoreService
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.avalanche.vms.StationsViewModel

class StationsActivity : ComponentActivity() {

    private val vm: StationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var search by remember { mutableStateOf("") }

            if (search.isNotEmpty())
                vm.load(this, search)

            AvalancheScaffold(activity = this, content = {
                AvalancheSection(title = "Search") {

                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Name") }
                    )

                    val stores: List<StoreService.GetStoresProto.Response> by vm.data.observeAsState(
                        emptyList()
                    )

                    AvalancheList(elements = stores, template = { store ->
                        
                        val logo = store.logo.toString()

                        val bytes = Base64.decode(logo, Base64.DEFAULT)

                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, logo.length)

                        if (bitmap == null) {
                            /* TODO: DO SOMETHING */
                        } else {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = store.name
                            )
                        }

                        AvalancheListElement(
                            onClick = {
                                // TODO: start StationActivity
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