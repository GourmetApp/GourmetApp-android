package com.jugarte.gourmet.data.chequegourmet;

import com.jugarte.gourmet.data.chequegourmet.request.BalanceRequest;
import com.jugarte.gourmet.data.chequegourmet.request.CardsRequest;
import com.jugarte.gourmet.data.chequegourmet.request.LoginRequest;
import com.jugarte.gourmet.data.chequegourmet.request.PaymentsRequest;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ChequeGourmetDataManager {

    public static final MediaType JSON = MediaType.parse("application/json");

    public ChequeGourmet getChequeGourmet(String username, String password)
            throws ConnectionException, EmptyException, NotFoundException {

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient client = httpBuilder
                .retryOnConnectionFailure(false)
                .build();

        LoginRequest loginRequest = new LoginRequest(client);
        String token = loginRequest.login(username, password);

        if (token == null) {
            throw new NotFoundException();
        }

        CardsRequest cardsRequest = new CardsRequest(client);
        String cardNumber = cardsRequest.getCards(token);

        BalanceRequest balanceRequest = new BalanceRequest(client);
        String balance = balanceRequest.getBalance(token);

        PaymentsRequest paymentsRequest = new PaymentsRequest(client);
        ChequeGourmet chequeGourmet = paymentsRequest.getPayments(token, balance, cardNumber);

        return chequeGourmet;
    }


}
