package com.jugarte.gourmet.data.chequegourmet.request;

import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmetBuilder;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright 2009-2018 Bitban Technologies, S.L.
 * All rights reserved.
 */
public class PaymentsRequest {

    private static final String PAYMENTS_URL = "https://gourmetpay.com/api/payments?format=json";
    private final OkHttpClient client;

    public PaymentsRequest(OkHttpClient client) {
        this.client = client;
    }

    public ChequeGourmet getPayments(String token, String cardNumber) throws
            ConnectionException, EmptyException, NotFoundException {

        Request request = new Request.Builder()
                .url(PAYMENTS_URL)
                .addHeader("x-auth-token", token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String paymentsResponse = response.body().string();
            ChequeGourmetBuilder chequeGourmetBuilder = new ChequeGourmetBuilder();
            return chequeGourmetBuilder.build(paymentsResponse, cardNumber);
        } catch (IOException e) {
            throw new ConnectionException();
        } catch (JSONException e) {
            throw new ConnectionException();
        }

    }
}
