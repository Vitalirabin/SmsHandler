package com.example.smsreeceiver.network

import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

class SendRepository() {
    fun pushData(url: String, data: RequestBody): Single<String> {
        return ApiFactory.getApi().pushData(url, data)
    }
}