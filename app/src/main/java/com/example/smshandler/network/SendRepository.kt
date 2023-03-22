package com.example.smshandler.network

import com.example.smshandler.PostModel
import io.reactivex.rxjava3.core.Single

class SendRepository() {
    fun pushDataWithToken(token: String, url: String, data: PostModel): Single<String> {
        return ApiFactory.getApi().pushDataWithToken(token, url, data)
    }

    fun pushDataWithLogin(
        login: String,
        password: String,
        url: String,
        data: PostModel
    ): Single<String> {
        return ApiFactory.getApi().pushDataWithLogin(login, password, url, data)
    }
}