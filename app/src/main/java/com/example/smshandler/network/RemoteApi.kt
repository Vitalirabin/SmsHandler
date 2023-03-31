package com.example.smshandler.network

import com.example.smshandler.PostModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface RemoteApi {
    @POST("api/handle/sms/")
    fun pushDataWithToken(
        @Header("Autorization") token: String,
        @Body body: PostModel
    ): Single<String>

    @POST("api/handle/sms/")
    fun pushDataWithLogin(
        @Header("username") userName: String,
        @Header("password") password: String,
        @Body body: PostModel
    ): Single<String>

    @POST("api/auth/sms/")
    fun authorizationWithLogin(@Body body: LoginAuthorizationModel): Single<String>

    @POST("api/auth/sms/")
    fun authorizationWithToken(@Body body: TokenAuthorizationModel): Single<String>
}