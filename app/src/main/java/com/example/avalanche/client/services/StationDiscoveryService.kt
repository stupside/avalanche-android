package com.example.avalanche.client.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class StationDiscoveryService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "channel.id.nfc"
        const val NOTIFICATION_CHANNEL_NAME = "channel.name.nfc"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "channel.description.nfc"
    }

    private val nfcReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            intent?.also {
                val messages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayExtra(
                        NfcAdapter.EXTRA_NDEF_MESSAGES,
                        NdefMessage::class.java
                    )
                } else {
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                        ?.also { parcelables ->
                            val messages: List<NdefMessage> = parcelables.map { it as NdefMessage }
                        }
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(StationDiscoveryService::class.java.name, "Nfc service started")

        registerReceiver(nfcReceiver, IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = NOTIFICATION_CHANNEL_DESCRIPTION
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(StationDiscoveryService::class.java.name)
            .setSmallIcon(androidx.core.R.drawable.notification_action_background)
            .setContentText("Nfc service started")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()

        with(NotificationManagerCompat.from(this)) {

            if (ActivityCompat.checkSelfPermission(
                    this@StationDiscoveryService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(111, notification)
            } else {
                Log.i(StationDiscoveryService::class.java.name, "Nfc service cannot notify")
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(nfcReceiver)


        Log.i(StationDiscoveryService::class.java.name, "Nfc service ended")

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancelAll()
    }
}