package com.jugarte.gourmet.data.chequegourmet;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChequeGourmetRequest {

    String getLogin(String url, Map<String, String> postParams) throws IOException {

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : postParams.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
