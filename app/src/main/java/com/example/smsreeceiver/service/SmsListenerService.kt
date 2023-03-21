package com.example.smsreeceiver.service

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.smsreeceiver.MainActivity
import com.example.smsreeceiver.R
import com.example.smsreeceiver.broadcast.SmsReceiver


class SmsListenerService : Service() {
    companion object {
        private const val CHANNEL_ID = "SmsListenerServiceToTgBot"
    }

    private val smsReceiver = SmsReceiver()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e("SmsService", "start")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val fl = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                PendingIntent.FLAG_MUTABLE
            else -> 0
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, fl)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setPriority(100)
                .build()
        } else {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.notification_message))
                .setContentIntent(pendingIntent)
                .setPriority(100)
                .build()
        }
        startForeground(234567, notification)
        startSmsReceiver()
        return START_NOT_STICKY
    }

    private fun startSmsReceiver() {
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        intentFilter.priority = 100
        registerReceiver(smsReceiver, intentFilter)
    }

    override fun onDestroy() {
        try {
            unregisterReceiver(smsReceiver)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}