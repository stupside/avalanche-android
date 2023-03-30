@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.avalanche

import Avalanche.Market.PlanService
import Avalanche.Market.StoreService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.shared.list.AvalancheList
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.StoreViewModel

class StoreActivity : ComponentActivity() {

    private lateinit var stationVm: StoreViewModel

    companion object {
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String): Intent {
            return Intent(context, StoreActivity::class.java).putExtra(StoreIdKey, storeId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!

        stationVm = StoreViewModel(storeId)

        stationVm.loadStore(this)
        stationVm.loadPlans(this)

        setContent {

            val storeState: StoreService.GetStoreProto.Response? by stationVm.store.observeAsState(
                null
            )

            val planStates: List<PlanService.GetPlansProto.Response> by stationVm.plans.collectAsState()

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Store")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        storeState?.let { store ->

                            StoreHeader(
                                context = this@StoreActivity,
                                storeId = storeId,
                                name = store.name,
                                description = store.description,
                                logo = store.logo.toString()
                            )

                            Column {

                                Text("Plans", style = MaterialTheme.typography.titleMedium)

                                AvalancheList(elements = planStates) { plan ->
                                    PlanItem(
                                        context = this@StoreActivity,
                                        storeId = storeId,
                                        planId = plan.planId,
                                        name = plan.name,
                                        description = "Plan description"
                                    )
                                }
                            }

                        }
                    }
                })
            }
        }
    }
}

@Composable
fun StoreHeader(context: Context, storeId: String, name: String, description: String, logo: String?) {
    Box(modifier = Modifier.padding(32.dp)) {
        Row {
            StoreLogo(logo)
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(description)
            }
        }
    }
}

@Composable
fun PlanItem(context: Context, storeId: String, planId: String, name: String, description: String) {

    val checkInIntent = PaymentCheckInActivity.getIntent(context, storeId, planId)

    ListItem(
        modifier = Modifier.clickable(onClick = {
            context.startActivity(checkInIntent)
        }),
        headlineText = { Text(name, style = MaterialTheme.typography.titleSmall) },
        supportingText = { Text(description, style = MaterialTheme.typography.bodyMedium) },
    )
}

@Composable
fun StoreLogo(logo: String?) {

    if (logo == null) {
        StoreLogoPlaceholder()
    } else {

        val bytes = logo.toByteArray()

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, logo.length)

        if (bitmap == null) {
            StoreLogoPlaceholder()
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = logo,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun StoreLogoPlaceholder() {
    Icon(
        imageVector = Icons.Rounded.Info,
        contentDescription = "Placeholder",
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
    )
}