package com.jugarte.gourmet.data.github;

import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.internal.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LastVersionBuilder {

    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String TAG_NAME_KEY = "tag_name";
    private static final String HTML_URL_KEY = "html_url";
    private static final String ASSETS_KEY = "assets";
    private static final String URL_KEY = "browser_download_url";
    private static final String CHANGELOG_KEY = "body";


    private String getChangelogWithHtmlCode(String text) {
        return text.replace("\r\n", "<br>");
    }

    public LastVersion build(String response) throws JSONException, ConnectionException {
        if (response == null || response.trim().length() == 0) {
            return null;
        }

        LastVersion lastVersion = null;
        JSONArray lastVersionJSONArray;

        try {
            lastVersionJSONArray = new JSONArray(response);
        } catch (JSONException e) {
            throw new ConnectionException();
        }

        if (lastVersionJSONArray.length() > 0) {
            JSONObject releaseObject = (JSONObject) lastVersionJSONArray.get(0);
            if (releaseObject != null) {
                lastVersion = new LastVersion();
                lastVersion.setIdVersion("" + releaseObject.getInt(ID_KEY));
                lastVersion.setNameTagVersion(releaseObject.getString(TAG_NAME_KEY));
                lastVersion.setNameVersion(releaseObject.getString(NAME_KEY));
                lastVersion.setUrlDownload(releaseObject.getString(HTML_URL_KEY));
                lastVersion.setUrlHomePage(Constants.getUrlHomePage());
                lastVersion.setChangelog(getChangelogWithHtmlCode(releaseObject.getString(CHANGELOG_KEY)));

                if (releaseObject.getJSONArray(ASSETS_KEY) != null &&
                        releaseObject.getJSONArray(ASSETS_KEY).length() > 0) {
                    JSONObject assetObject = (JSONObject) releaseObject.getJSONArray(ASSETS_KEY).get(0);
                    if (assetObject != null) {
                        lastVersion.setUrlDownload(assetObject.getString(URL_KEY));
                        lastVersion.setIdDownload("" + assetObject.getInt(ID_KEY));
                        lastVersion.setNameDownload(assetObject.getString(NAME_KEY));
                    }
                }
            }
        }

        return lastVersion;
    }

}
