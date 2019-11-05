package com.jugarte.gourmet.data.gourmetpay

import com.jugarte.gourmet.data.gourmetpay.services.*
import io.reactivex.Single

class GourmetPayRepository {

    fun login(user: String, password: String): Single<User> {
        return LoginService().login(user, password)
    }

    fun getBalance(accessToken: String): Single<String> {
        return BalanceService(accessToken).getBalance()
    }

    fun getPayments(accessToken: String): Single<Payments> {
        return PaymentsService(accessToken).getPayments()
    }

}