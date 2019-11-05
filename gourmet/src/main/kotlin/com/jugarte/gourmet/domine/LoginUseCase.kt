package com.jugarte.gourmet.domine

import com.jugarte.gourmet.data.gourmetpay.GourmetPayRepository
import com.jugarte.gourmet.data.gourmetpay.services.Payments
import io.reactivex.Single

class LoginUseCase {

    private var accessToken: String? = null
    private var balance: String? = null

    fun execute(user: String, password: String): Single<LoginResponse> {
        val repository = GourmetPayRepository()
        return repository
                .login(user, password)
                .flatMap {
                    accessToken = it.accessToken
                    repository.getBalance(accessToken!!)
                }.flatMap {
                    balance = it
                    repository.getPayments(accessToken!!)
                }.map {
                    LoginResponse(balance!!, it)
                }
    }

}

data class LoginResponse(
        val current: String,
        val payments: Payments
)


