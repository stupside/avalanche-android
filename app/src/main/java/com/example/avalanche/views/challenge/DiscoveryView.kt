package com.example.avalanche.views.challenge

import Avalanche.Passport.TicketService.GetTicketProto
import AvalancheQrScanner
import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.WalletsActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.core.ui.shared.AvalancheGoBackButton
import com.example.avalanche.views.ticket.TicketHeader
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

@Composable
@ExperimentalGetImage
fun DiscoveryView(context: Context, viewModel: DiscoveryViewModel) {

    val deviceIdentifier = Constants.DEVICE_IDENTIFIER

    val preferences = Constants.getSharedPreferences(context)

    var storeId by remember {
        mutableStateOf<String?>(null)
    }

    val ticket: GetTicketProto.Response? by viewModel.ticket.observeAsState()

    LaunchedEffect(storeId) {
        storeId?.let {

            viewModel.loadTicket(context, it, deviceIdentifier)

            val editor = preferences?.edit()

            editor?.putString(
                Constants.AVALANCHE_SHARED_PREFERENCES_STORE,
                storeId
            )

            editor?.apply()

            val intent = WalletsActivity.getIntent(context)

            context.startActivity(intent)
        }
    }

    val scanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().enableAllPotentialBarcodes()
                .build()
        )
    }

    val executor = remember {
        Executors.newSingleThreadExecutor()
    }

    Scaffold(topBar = {
        DiscoveryTopBar(context)
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ticket?.let { ticket ->
                        TicketHeader(ticketName = ticket.name)
                    }

                    Text(
                        "Scan the QR Code of the store",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Box(modifier = Modifier.size(250.dp, 250.dp)) {
                    AvalancheQrScanner(
                        context = context,
                        executor = executor,
                        scanner = scanner,
                        onDiscovered = { barcode ->
                            when (barcode.valueType) {
                                Barcode.TYPE_TEXT -> {
                                    barcode.rawValue?.let { value ->
                                        storeId = value
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    })
}

@Composable
fun DiscoveryTopBar(context: Context) {
    TopAppBar(title = {
        Text("Discovery")
    }, navigationIcon = {
        AvalancheGoBackButton(context)
    })
}