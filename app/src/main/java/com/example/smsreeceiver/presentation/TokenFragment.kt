package com.example.smsreeceiver.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.smsreeceiver.databinding.FragmentTokenBinding

class TokenFragment : Fragment() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 416
    }

    private val permissions = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    )

    private lateinit var binding: FragmentTokenBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokenBinding.inflate(inflater, container, false)
        return binding.root
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