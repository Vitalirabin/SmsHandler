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
            url = context?.getSharedPreferences(Constants.SP, Context.MODE_PRIVATE)
                ?.getString(Constants.URL_KEY, "").toString()
            val mMessage = String.format(
                "%s%s&%s%s",
                context?.getString(R.string.member),
                message.originatingAddress.toString(),
                context?.getString(R.string.message),
                message.messageBody
            )
            val pushData = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded"),
                mMessage
            )
            SendRepository().pushData(
                url,
                pushData
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    Log.e(TAG, "onReceive->${it}")
                }, {
                    Log.e(TAG, it.message, it)
                })

        }
    }
}