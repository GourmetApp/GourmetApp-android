package com.jugarte.gourmet.data.gourmetpay.services

import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rxResponseObject
import com.jugarte.gourmet.Deserializer
import io.reactivex.Single

class LoginService {

    companion object {
        private const val URL = "https://gourmetpay.com/api/payments?format=json"
    }

    fun login(user: String, password: String): Single<User> {
        val bodyJson = """
            { 
                "username" : "$user",
                "password" : "$password"
            }
            """
        return URL.httpPost()
                .jsonBody(bodyJson)
                .rxResponseObject(Deserializer(User::class.java))
    }

}

data class User(
        val username: String,
        val accessToken: String
)
