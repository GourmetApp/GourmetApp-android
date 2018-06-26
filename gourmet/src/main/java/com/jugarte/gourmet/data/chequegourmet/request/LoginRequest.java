package com.jugarte.gourmet.data.chequegourmet.request;

import com.jugarte.gourmet.exceptions.ConnectionException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jugarte.gourmet.data.chequegourmet.ChequeGourmetDataManager.JSON;


public class LoginRequest {

    private static final String LOGIN_URL = "https://gourmetpay.com/api/login";
    private final OkHttpClient client;

    public LoginRequest(OkHttpClient client) {
        this.client = client;
    }

    public String login(String userName, String password) throws ConnectionException {
        RequestBody body = RequestBody.create(JSON, "{\"username\":\"" + userName + "\", \"password\":\"" + password + "\"}");

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        String token = null;
        try {
            Response response = client.newCall(request).execute();
            String loginResponse = response.body().string();
            token = getToken(loginResponse);
        } catch (IOException e) {
            throw new ConnectionException();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return token;
    }

    private String getToken(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("access_token");
    }

}
