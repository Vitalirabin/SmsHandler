package com.example.smshandler.network

import com.example.smshandler.PostModel
import io.reactivex.rxjava3.core.Single

class SendRepository() {
    fun pushDataWithToken(token: String, data: PostModel): Single<String> {
        return ApiFactory.getApi().pushDataWithToken(token, data)
    }

    fun pushDataWithLogin(
        login: String,
        password: String,
        data: PostModel
    ): Single<String> {
        return ApiFactory.getApi().pushDataWithLogin(login, password, data)
    }

    fun authorizationWithLogin(loginAuthorizationModel: LoginAuthorizationModel): Single<String> {
        return ApiFactory.getApi().authorizationWithLogin(loginAuthorizationModel)
    }

    fun authorizationWithToken(tokenAuthorizationModel: TokenAuthorizationModel): Single<String> {
        return ApiFactory.getApi().authorizationWithToken(tokenAuthorizationModel)
    }
}