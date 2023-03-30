package com.example.smshandler.presentation

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.smshandler.Constants.NUMBER
import com.example.smshandler.Constants.SP
import com.example.smshandler.Constants.TOKEN
import com.example.smshandler.Constants.TOKEN_OR_LOGIN
import com.example.smshandler.R
import com.example.smshandler.databinding.FragmentTokenBinding
import com.example.smshandler.service.SmsListenerService

class TokenFragment : Fragment() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 416
    }

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PRECISE_PHONE_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG
        )
    } else {
        arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG
        )
    }

    private lateinit var binding: FragmentTokenBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTokenBinding.inflate(inflater, container, false)
        checkPermissions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isServiceRunning(SmsListenerService::class.java)) {
            binding.textInputToken.visibility = View.INVISIBLE
            binding.textInputNumber.visibility = View.INVISIBLE
            binding.startServiceWithTokenButton.visibility = View.INVISIBLE
            binding.startServiceWithTokenButton.isClickable = false
            binding.serviceWorkedMessage.visibility=View.VISIBLE
        }
        binding.startServiceWithTokenButton.setOnClickListener {
            onClickStartService()
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun onClickStartService() {
        val token = binding.textInputToken.text.toString()
        val number = binding.textInputNumber.text.toString()
        if (token != "" || number != "") {
            context?.getSharedPreferences(SP, Context.MODE_PRIVATE)?.edit()
                ?.putBoolean(TOKEN_OR_LOGIN, true)
                ?.putString(TOKEN, token)
                ?.putString(NUMBER, number)
                ?.apply()
            startSmsListenerService()
        } else Toast.makeText(context, getString(R.string.enter_all), Toast.LENGTH_SHORT).show()
    }

    private fun startSmsListenerService() {
        requireContext().stopService(
            Intent(
                requireContext(),
                SmsListenerService::class.java
            )
        )
        requireContext().startForegroundService(
            Intent(
                requireContext(),
                SmsListenerService::class.java
            )
        )
    }

    private fun checkPermissions() {
        activity?.let { mActivity ->
            permissions.forEach {
                if (ActivityCompat.checkSelfPermission(
                        mActivity,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        mActivity, permissions,
                        PERMISSION_REQUEST_CODE
                    )
                    return
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            checkPermissions()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}