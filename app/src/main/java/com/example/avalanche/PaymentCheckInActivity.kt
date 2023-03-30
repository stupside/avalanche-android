package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.viewmodels.StoreViewModel

class PaymentCheckInActivity : ComponentActivity() {

    companion object {
        private const val PlanIdKey = "PlanId"

        fun getIntent(context: Context, planId: String): Intent {
            return Intent(context, PaymentCheckInActivity::class.java)
                .putExtra(PlanIdKey, planId)
        }
    }

    private lateinit var storeVm: StoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storeVm.loadPlans(this)

        setContent {

            var ticketName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            val inputDay = remember { mutableStateOf(0) }
            val inputMonth = remember { mutableStateOf(0) }
            val inputYear = remember { mutableStateOf(0) }

            AvalancheTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Payments")
                    }, navigationIcon = {
                        AvalancheGoBackButton(activity = this)
                    })
                }, content = { paddingValues ->

                    Column(modifier = Modifier.padding(paddingValues)) {

                        Column {
                            OutlinedTextField(
                                value = ticketName,
                                placeholder = {
                                    Text("Ticket name")
                                },
                                onValueChange = { ticketName = it },
                            )



                        }



                        AndroidView(
                            { CalendarView(it) },
                            modifier = Modifier.wrapContentSize(),
                            update = { views ->
                                views.setOnDateChangeListener { _, year, month, day ->
                                    inputYear.value = year
                                    inputMonth.value = month
                                    inputDay.value = day
                                }
                            }
                        )
                    }
                })
            }
        }
    }
}