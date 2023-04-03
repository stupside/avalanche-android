package com.example.avalanche.views.nfc

import Avalanche.Market.StoreService.GetStoreProto
import Avalanche.Passport.TicketService.GetTicketProto
import AvalancheQrScanner
import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avalanche.StoresActivity
import com.example.avalanche.core.environment.Constants
import com.example.avalanche.views.stores.StoreHeader
import com.example.avalanche.views.ticket.TicketTopBar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

@Composable
@ExperimentalGetImage
fun NfcView(context: Context, viewModel: NfcViewModel) {

    var storeId by remember {
        mutableStateOf<String?>(null)
    }

    Constants.getSharedPreferences(context)?.getString(Constants.STORE_ID, storeId)

    LaunchedEffect(storeId) {
        storeId?.let {
            viewModel.loadStore(context, it)
            viewModel.loadTicket(context, it)
        }
    }

    val store: GetStoreProto.Response? by viewModel.store.observeAsState()
    val ticket: GetTicketProto.Response? by viewModel.ticket.observeAsState()

    Scaffold(topBar = {
        TicketTopBar(context)
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                var dialog by remember {
                    mutableStateOf(false)
                }

                Button({
                    dialog = true
                }) {
                    Text("Simulate nfc")
                }

                if (dialog) {
                    AlertDialog({ }) {
                        Surface(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp),
                            shape = MaterialTheme.shapes.large
                        ) {

                            val scanner = BarcodeScanning.getClient(
                                BarcodeScannerOptions.Builder().enableAllPotentialBarcodes()
                                    .build()
                            )
                            val executor = Executors.newSingleThreadExecutor()

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Scan the qr code of your store")
                                AvalancheQrScanner(
                                    context = context,
                                    executor = executor,
                                    scanner = scanner,
                                    onDiscovered = {
                                        when (it.valueType) {
                                            Barcode.TYPE_TEXT -> {

                                                dialog = false

                                                storeId = it.rawValue
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                store?.let {

                    StoreHeader(
                        name = it.name,
                        description = it.description,
                        logo = it.logo.toString()
                    )

                    if (ticket == null) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No ticket sealed for this store",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedButton(onClick = {
                                val storesIntent = StoresActivity.getIntent(context, storeId)

                                context.startActivity(storesIntent)
                            }) {
                                Text("Buy now")
                            }
                        }
                    } else {
                        Text("Show info of ticket ${ticket?.name}")
                        // TODO: grpc challenge and show if valid on screen
                        // TODO: on wallets, show the current ticket in use, regardless of the current station
                        // TODO: Session.Track with a background service
                    }
                }
            }
        }
    })
}