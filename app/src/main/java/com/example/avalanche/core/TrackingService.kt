package com.example.avalanche.core

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder


class TrackingServiceListener : LocationListener {

    override fun onLocationChanged(location: Location) {

    }

}

class TrackingService : Service() {

    companion object {

        private const val CHANNEL_ID = "avalanche.tracking"
        private const val CHANNEL_NAME = "avalanche.tracking"
        private const val CHANNEL_DESCRIPTION = "avalanche.tracking"

        private const val TRACKING_PRECISION_IN_METERS = 10f
        private const val TRACKING_PRECISION_IN_MS = 10_000L

        private const val TITLE = "Tracking"
        private const val DESCRIPTION = "Tracking"
    }

    private lateinit var locations: LocationManager

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()

        locations = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()

        val pendingIntent = Intent(this, TrackingService::class.java).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(TITLE)
            .setContentText(DESCRIPTION)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)


        val listener = TrackingServiceListener()

        locations.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            TRACKING_PRECISION_IN_MS,
            TRACKING_PRECISION_IN_METERS,
            listener
        )

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {

        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }
}