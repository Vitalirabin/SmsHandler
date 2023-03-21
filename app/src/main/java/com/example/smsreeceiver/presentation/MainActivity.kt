package com.example.smsreeceiver.presentation

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.smsreeceiver.Constants
import com.example.smsreeceiver.R
import com.example.smsreeceiver.databinding.ActivityMainBinding
import com.example.smsreeceiver.service.SmsListenerService

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    private fun startSmsListenerService() {
        if (!isServiceRunning(SmsListenerService::class.java)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, SmsListenerService::class.java))
            } else startService(Intent(this, SmsListenerService::class.java))
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }




}
