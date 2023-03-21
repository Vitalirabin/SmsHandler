package com.example.smsreeceiver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.smsreeceiver.Constants

private const val TAG = "CallReceiver"

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            val incomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val url = context?.getSharedPreferences(Constants.SP, Context.MODE_PRIVATE)
                ?.getString(Constants.URL_KEY, "").toString()
            incomingNumber?.let { Log.d(TAG, incomingNumber) }
            /* val mMessage = String.format(
                 "%s%s",
                 context?.getString(R.string.member),
                 incomingNumber
             )
             val pushData = RequestBody.create(
                 MediaType.parse("application/x-www-form-urlencoded"),
                 mMessage
             )
             SendRepository().pushData(url, pushData).subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .unsubscribeOn(Schedulers.io())
                 .subscribe({
                     Log.e(TAG, "onReceive->${it}")
                 }, {
                     Log.e(TAG, it.message, it)
                 })*/
        }
    }
}