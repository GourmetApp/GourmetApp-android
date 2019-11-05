package com.jugarte.gourmet.data.gourmetpay.services

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rxResponseObject
import com.jugarte.gourmet.Deserializer
import io.reactivex.Single
import java.util.*

class PaymentsService(private val accessToken: String) {

    companion object {
        private const val URL = "https://gourmetpay.com/api/payments?format=json"
    }

    fun getPayments(): Single<Payments> {
        return URL.httpGet()
                .header("x-auth-token", accessToken)
                .rxResponseObject(Deserializer(Payments::class.java))

    }

}

data class Payments(
        val payments: List<Payment>
)

data class Payment(
        val dateCreated: Date,
        val effectiveDate: Date,
        val hasPromo: Boolean,
        val dsAmount: String,
        val id: Int,
        val transactionCode: String,
        val type: String,
        val card: String,
        val cardCG: Boolean
)

data class Restaurant(
        val name: String
)
