package com.example.avalanche.client.views

import Avalanche.StationGrpcGrpcKt
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avalanche.client.services.LocationService
import com.example.avalanche.client.services.StationDiscoveryService
import com.example.avalanche.client.views.ui.theme.AvalancheTheme
import io.grpc.ManagedChannelBuilder

class MainActivity : ComponentActivity() {

    lateinit var stub: StationGrpcGrpcKt.StationGrpcCoroutineStub

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        val channel = ManagedChannelBuilder.forAddress("https://corporation.avalanche", 8080).usePlaintext().build()

        stub = StationGrpcGrpcKt.StationGrpcCoroutineStub(channel)

                 */


        val station = Intent(this, StationDiscoveryService::class.java)

        val location = with(Intent(this, LocationService::class.java)) {
            putExtra(LocationService.LOCATION_UPDATE_RATE_KEY, 5000L)
            putExtra(LocationService.LOCATION_UPDATE_RATE_FASTEST_KEY, 1000L)
        }

        setContent {
            AvalancheTheme {
                Scaffold(
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = paddingValues.calculateBottomPadding())
                                .verticalScroll(rememberScrollState())
                        ) {

                            Row {
                                Button(onClick = {
                                    startService(location)
                                }) {
                                    Text(text = "Start location service")
                                }

                                Button(onClick = {
                                    stopService(location)
                                }) {
                                    Text(text = "Stop location service")
                                }
                            }

                            Row {
                                Button(onClick = {
                                    startService(station)
                                }) {
                                    Text(text = "Station discovery service")
                                }

                                Button(onClick = {
                                    stopService(station)
                                }) {
                                    Text(text = "Station discovery service")
                                }
                            }

                        }
                    },
                    floatingActionButton = {
                        ResearchStation(this)
                    }
                )
            }
        }
    }
}

@Composable
fun ResearchStation(context: Context) {

    val search = Intent(context, StationSearchActivity::class.java)

    val research = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {

        })

    FloatingActionButton(onClick = {

        research.launch(search);

        Toast.makeText(context, "Open search", Toast.LENGTH_SHORT).show()
    }) {
        Text(text = "Search station")
    }
}