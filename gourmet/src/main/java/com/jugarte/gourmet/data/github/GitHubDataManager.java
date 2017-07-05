package com.jugarte.gourmet.data.github;

import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.exceptions.ConnectionException;

import org.json.JSONException;

import java.io.IOException;

public class GitHubDataManager {

    private static final String USER = "gourmetapp";
    private static final String PROJECT = "GourmetApp-android";

    public LastVersion getLastVersion() throws ConnectionException, JSONException, IOException {

        GitHubRequest gitHubRequest = new GitHubRequest();
        String response = gitHubRequest.getReleases(USER, PROJECT);
        return new LastVersionBuilder().build(response);
    }
}
