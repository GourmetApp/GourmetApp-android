package com.jugarte.gourmet.data.github;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GitHubRequest {

    private static final String RELEASES = "https://api.github.com/repos/%s/%s/releases";

    String getReleases(String user, String project) throws IOException {

        String url = String.format(RELEASES, user, project);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
