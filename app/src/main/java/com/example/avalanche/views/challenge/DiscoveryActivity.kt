package com.example.avalanche.views.challenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.ExperimentalGetImage
import com.example.avalanche.core.ui.theme.AvalancheTheme
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class DiscoveryActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DiscoveryActivity::class.java)
        }
    }

    private val discoveryVm: DiscoveryViewModel by viewModels()

    private lateinit var executor: ExecutorService
    private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        executor = Executors.newSingleThreadExecutor()

        scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().enableAllPotentialBarcodes()
                .build()
        )

        setContent {
            AvalancheTheme {
                DiscoveryView(
                    context = this,
                    viewModel = discoveryVm,
                    executor = executor,
                    scanner = scanner
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        executor.shutdown()
        scanner.close()
    }
}