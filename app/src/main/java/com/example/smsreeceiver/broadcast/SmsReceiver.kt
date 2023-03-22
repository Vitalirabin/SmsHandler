package com.example.smsreeceiver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.telephony.SmsMessage
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import com.example.smsreeceiver.Constants.LOGIN
import com.example.smsreeceiver.Constants.NUMBER
import com.example.smsreeceiver.Constants.PASSWORD
import com.example.smsreeceiver.Constants.SP
import com.example.smsreeceiver.Constants.TOKEN
import com.example.smsreeceiver.Constants.TOKEN_OR_LOGIN
import com.example.smsreeceiver.Constants.URL
import com.example.smsreeceiver.PostModel
import com.example.smsreeceiver.network.SendRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG = "SmsReceiver"

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pduArray = intent?.extras!!["pdus"] as Array<Any>?
        for (i in pduArray!!.indices) {
            val wm =
                context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val message = SmsMessage.createFromPdu(pduArray[i] as ByteArray)
            val tokenOrLogin =
                context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                    ?.getBoolean(TOKEN_OR_LOGIN, false)
            val login =
                context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(LOGIN, "") ?: ""
            val password =
                context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(PASSWORD, "")
                    ?: ""
            val token =
                context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(TOKEN, "") ?: ""
            val phoneNumber =
                context.getSharedPreferences(SP, Context.MODE_PRIVATE)?.getString(NUMBER, "") ?: ""
            val sender = message.originatingAddress ?: ""
            val mMessage = message.messageBody ?: ""
            val ip: String = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

            val data = PostModel("1.0.0", sender, mMessage, phoneNumber, ip)
            val repository = SendRepository()
            if (tokenOrLogin == true) {
                repository.pushDataWithToken(token, URL, data).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(context, "Пришёл ответ", Toast.LENGTH_SHORT).show()
                    }, {
                        Log.e(TAG, it.message, it)
                        Toast.makeText(context, "Пришёл пустой ответ", Toast.LENGTH_SHORT).show()
                    })
            } else {
                repository.pushDataWithLogin(login, password, URL, data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(context, "Пришёл ответ", Toast.LENGTH_SHORT).show()
                    }, {
                        Log.e(TAG, it.message, it)
                        Toast.makeText(context, "Пришёл пустой ответ", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }
}