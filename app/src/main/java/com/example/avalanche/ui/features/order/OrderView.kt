package com.example.avalanche.ui.features.order

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import avalanche.merchant.plan.Plan.GetOnePlanRpc
import avalanche.merchant.store.Store.GetOneStoreRpc
import com.example.avalanche.ui.components.AvalancheGoBackButton
import java.util.concurrent.TimeUnit

@Composable
fun OrderView(viewModel: OrderViewModel, planId: String, goBack: () -> Unit, goWallet: () -> Unit) {

    LaunchedEffect(planId) {
        viewModel.loadPlan(planId)
    }

    val store: GetOneStoreRpc.Response? by viewModel.store.observeAsState()
    val plan: GetOnePlanRpc.Response? by viewModel.plan.observeAsState()

    val picker = rememberDatePickerState()

    val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    val activation = if (picker.selectedDateMillis == null) {
        0
    } else {
        TimeUnit.MILLISECONDS.toDays(picker.selectedDateMillis!! - now.timeInMillis)
    }

    Scaffold(topBar = {

        TopAppBar(title = {

            plan?.let {
                Text("Order ${it.name}")
            }
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {

                store?.let {
                    ListItem(
                        headlineContent = {
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "Store"
                            )
                        }, supportingContent = {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                text = it.name
                            )
                        }
                    )
                }

                plan?.let {
                    ListItem(
                        headlineContent = {
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "Price"
                            )
                        }, supportingContent = {
                            if (it.isFree)
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    text = "Free"
                                )
                            else
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    text = "${it.price / 100} $"
                                )
                        }
                    )

                    ListItem(
                        headlineContent = {
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "Duration"
                            )
                        }, supportingContent = {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                text = "${TimeUnit.SECONDS.toDays(it.duration.seconds)} days"
                            )
                        }
                    )
                }

                DatePicker(
                    state = picker,
                    dateValidator = { utcDateInMills ->
                        utcDateInMills > now.timeInMillis
                    },
                    showModeToggle = false
                )

                plan?.let {
                    Button(
                        enabled = activation >= 0,
                        onClick = {
                            viewModel.orderPlan(it.planId, activation.toInt()) {
                                goWallet()
                            }
                        }) {
                        Text("Order '${it.name}' (available in $activation days)")
                    }
                }
            }
        }
    }
}
