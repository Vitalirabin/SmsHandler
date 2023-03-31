package com.example.smshandler.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.smshandler.service.SmsListenerService

class BootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            it.startForegroundService(
                Intent(
                    it,
                    SmsListenerService::class.java
                )
            )
        }
    }

}