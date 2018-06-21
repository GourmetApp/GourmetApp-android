package com.jugarte.gourmet.data.chequegourmet.request;

import com.jugarte.gourmet.data.chequegourmet.ChequeGourmetBuilder;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright 2009-2018 Bitban Technologies, S.L.
 * All rights reserved.
 */
public class CardsRequest {

    private static final String CARDS_URL = "https://gourmetpay.com/api/cards?format=json";
    private final OkHttpClient client;

    public CardsRequest(OkHttpClient client) {
        this.client = client;
    }

    public String getCards(String token) throws ConnectionException, EmptyException {

        Request request = new Request.Builder()
                .url(CARDS_URL)
                .addHeader("x-auth-token", token)
                .build();

        String cardNumber;
        try {
            Response response = client.newCall(request).execute();
            String paymentsResponse = response.body().string();
            ChequeGourmetBuilder chequeGourmetBuilder = new ChequeGourmetBuilder();
            cardNumber = chequeGourmetBuilder.buildCards(paymentsResponse);
        } catch (IOException e) {
            throw new ConnectionException();
        } catch (JSONException e) {
            throw new ConnectionException();
        }

        return cardNumber;
    }

}
