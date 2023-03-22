package com.example.smshandler.network

import com.example.smshandler.PostModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface RemoteApi {
    @POST
    fun pushDataWithToken(
        @Header("Autorization") token: String,
        @Url url: String,
        @Body body: PostModel
    ): Single<String>

    @POST
    fun pushDataWithLogin(
        @Header("username") userName: String,
        @Header("password") password: String,
        @Url url: String,
        @Body body: PostModel
    ): Single<String>
}