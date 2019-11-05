package com.jugarte.gourmet.data.gourmetpay.services

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rxResponseObject
import com.jugarte.gourmet.Deserializer
import io.reactivex.Single

class BalanceService(val accessToken: String) {
    companion object {
        private const val URL = "https://gourmetpay.com/api/base/getBalance?format=json"
    }

    fun getBalance(): Single<String> {
        return URL.httpGet()
                .header("x-auth-token", accessToken)
                .rxResponseObject(Deserializer(Balance::class.java))
                .map { it.balance }

    }
}

data class Balance(val balance: String)