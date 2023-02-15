package com.example.avalanche.client.services

import Avalanche.WalletService
import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationService : Service() {

    private val locationUpdateCallback = LocationUpdateCallback()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        const val LOCATION_UPDATE_RATE_KEY = "location.update.rate"
        const val LOCATION_UPDATE_RATE_FASTEST_KEY = "location.update.rate.fastest"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    class LocationUpdateCallback : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            Log.i(LocationService::class.java.name, location.lastLocation.toString())

            super.onLocationResult(location)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        Log.i(LocationService::class.java.name, "Location service started")

        val canAccessCoarseLocation = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val canAccessFineLocation = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if(canAccessCoarseLocation && canAccessFineLocation){

            val intervalRate: Long = intent.getLongExtra(LOCATION_UPDATE_RATE_KEY, 0)
            val fastestIntervalRate: Long = intent.getLongExtra(LOCATION_UPDATE_RATE_FASTEST_KEY, 0)

            val locationRequest = LocationRequest.create().apply {
                interval = intervalRate
                fastestInterval = fastestIntervalRate
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdateCallback, Looper.getMainLooper())
        }
        else {
            Log.i(LocationService::class.java.name, "Location service doesn't have location permissions granted")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.i(LocationService::class.java.name, "Location service stopped")

        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback)

        super.onDestroy()
    }
}