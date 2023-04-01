package com.example.smshandler.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.SmsMessage
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import com.example.smshandler.Constants.ENABLE
import com.example.smshandler.Constants.LOGIN
import com.example.smshandler.Constants.NUMBER
import com.example.smshandler.Constants.PASSWORD
import com.example.smshandler.Constants.SP
import com.example.smshandler.Constants.TOKEN
import com.example.smshandler.Constants.TOKEN_OR_LOGIN
import com.example.smshandler.PostModel
import com.example.smshandler.network.SendRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


private const val TAG = "SmsReceiver"

class SmsReceiver : BroadcastReceiver() {
    @SuppressLint("CheckResult", "UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive")
        if (context?.getSharedPreferences(SP, Context.MODE_PRIVATE)
                ?.getBoolean(ENABLE, false) == true
        ) {
            val pduArray = intent?.extras!!["pdus"] as Array<Any>?
            for (i in pduArray!!.indices) {
                val wm =
                    context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val message = SmsMessage.createFromPdu(pduArray[i] as ByteArray)
                val tokenOrLogin =
                    context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                        ?.getBoolean(TOKEN_OR_LOGIN, false)
                val login =
                    context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(LOGIN, "")
                        ?: ""
                val password =
                    context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(PASSWORD, "")
                        ?: ""
                val token =
                    context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(TOKEN, "")
                        ?: ""
                val phoneNumber =
                    context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(NUMBER, "")
                        ?: ""
                val sender = message.originatingAddress ?: ""
                val mMessage = message.messageBody ?: ""
                val ip: String = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

                var whichSIM = 0 // this for security fallback to SIM 1
                if (intent.extras!!.containsKey("subscription")) {
                    whichSIM = intent.extras!!.getInt("subscription")
                }
                Log.e(TAG, message.messageBody)
                Toast.makeText(context, "$whichSIM", Toast.LENGTH_LONG).show()
                val data = PostModel("1.0.0", sender, mMessage, phoneNumber, ip)
                val repository = SendRepository()
                if (tokenOrLogin == true) {
                    repository.pushDataWithToken(token, data).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe({
                            //     Toast.makeText(context, "Пришёл ответ", Toast.LENGTH_SHORT).show()
                        }, {
                            Log.e(TAG, it.message, it)
                            //    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        })
                } else {
                    repository.pushDataWithLogin(login, password, data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe({
                            //   Toast.makeText(context, "Пришёл ответ", Toast.LENGTH_SHORT).show()
                        }, {
                            Log.e(TAG, it.message, it)
                            //   Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }
    }
}