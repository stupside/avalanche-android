package com.example.avalanche.ui.features.order

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.ui.components.AvalancheGoBackButton

@Composable
fun OrderView(viewModel: OrderViewModel, planId: String, goBack: () -> Unit) {

    var days by rememberSaveable { mutableStateOf(0) }

    val picker = rememberDatePickerState()

    val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    Scaffold(topBar = {

        TopAppBar(title = {
            Text("Payment")
        }, navigationIcon = {
            AvalancheGoBackButton(goBack)
        })
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DatePicker(
                    state = picker,
                    dateValidator = { utcDateInMills ->
                        utcDateInMills >= now.timeInMillis
                    },
                )
            }
        }
    }
}
