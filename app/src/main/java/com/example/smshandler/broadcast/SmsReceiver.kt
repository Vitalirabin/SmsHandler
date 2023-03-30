package com.example.smshandler.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.SmsMessage
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import com.example.smshandler.Constants.LOGIN
import com.example.smshandler.Constants.NUMBER
import com.example.smshandler.Constants.PASSWORD
import com.example.smshandler.Constants.SP
import com.example.smshandler.Constants.TOKEN
import com.example.smshandler.Constants.TOKEN_OR_LOGIN


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

            var whichSIM = 0 // this for security fallback to SIM 1
            if (intent.extras!!.containsKey("subscription")) {
                whichSIM = intent.extras!!.getInt("subscription")
            }
            val bundle: Bundle? = intent.extras
            val slot: Int? = bundle?.getInt("slot", -1)
            Log.d("slot", slot.toString())
          //  Toast.makeText(context, "$slot", Toast.LENGTH_LONG).show()
            /* val data = PostModel("1.0.0", sender, mMessage, phoneNumber, ip)
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
             }*/
        }
    }
}