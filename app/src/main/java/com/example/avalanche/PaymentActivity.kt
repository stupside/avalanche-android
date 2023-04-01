package com.example.avalanche

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.example.avalanche.views.payment.PaymentView
import com.example.avalanche.views.payment.PaymentViewModel


class PaymentActivity : ComponentActivity() {

    companion object {
        private const val PlanIdKey = "PlanId"
        private const val StoreIdKey = "StoreId"

        fun getIntent(context: Context, storeId: String, planId: String): Intent {
            return Intent(context, PaymentActivity::class.java)
                .putExtra(StoreIdKey, storeId)
                .putExtra(PlanIdKey, planId)
        }
    }

    private val paymentVm: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getStringExtra(StoreIdKey)!!
        val planId = intent.getStringExtra(PlanIdKey)!!

        setContent {
            AvalancheTheme {
                PaymentView(this, paymentVm, storeId, planId)
            }
        }
    }
}