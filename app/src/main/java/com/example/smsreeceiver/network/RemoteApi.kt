package com.example.smsreeceiver.network

import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface RemoteApi {
    @POST
    fun pushData(@Url url: String, @Body body: RequestBody): Single<String>
}