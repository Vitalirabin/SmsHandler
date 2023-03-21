package com.example.smsreeceiver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.telephony.SmsMessage
import android.util.Log
import com.example.smsreeceiver.Constants
import com.example.smsreeceiver.R
import com.example.smsreeceiver.network.SendRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody

private const val TAG = "SmsReceiver"

class SmsReceiver : BroadcastReceiver() {
    var url = ""
    override fun onReceive(context: Context?, intent: Intent?) {

        val pduArray = intent?.extras!!["pdus"] as Array<Any>?
        for (i in pduArray!!.indices) {
            val message = SmsMessage.createFromPdu(pduArray[i] as ByteArray)

        }
    }
}