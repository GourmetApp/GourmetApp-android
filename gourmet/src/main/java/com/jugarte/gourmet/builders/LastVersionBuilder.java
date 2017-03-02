package com.jugarte.gourmet.builders;

import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.internal.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LastVersionBuilder extends BaseBuilder {

    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String TAG_NAME_KEY = "tag_name";
    private static final String HTML_URL_KEY = "html_url";
    private static final String ASSSETS_KEY = "assets";
    private static final String URL_KEY = "browser_download_url";
    private static final String CHANGELOG_KEY = "body";

    private String _data = "";

    private String getChangelogWithHtmlCode(String text) {
        return text.replace("\r\n", "<br>");
    }

    @Override
    public Object build() throws Exception {
        if (_data == null || _data.trim().length() == 0) {
            return null;
        }

        LastVersion lastVersion = null;
        JSONArray lastVersionJSONArray = null;

        try {
            lastVersionJSONArray = new JSONArray(this._data);
        } catch (JSONException e) {
            return null;
        }

        if (lastVersionJSONArray != null && lastVersionJSONArray.length() > 0) {
            JSONObject releaseObject = (JSONObject) lastVersionJSONArray.get(0);
            if (releaseObject != null) {
                lastVersion = new LastVersion();
                lastVersion.setIdVersion("" + releaseObject.getInt(ID_KEY));
                lastVersion.setNameTagVersion(releaseObject.getString(TAG_NAME_KEY));
                lastVersion.setNameVersion(releaseObject.getString(NAME_KEY));
                lastVersion.setUrlDownload(releaseObject.getString(HTML_URL_KEY));
                lastVersion.setUrlHomePage(Constants.getUrlHomePage());
                lastVersion.setChangelog(getChangelogWithHtmlCode(releaseObject.getString(CHANGELOG_KEY)));

                if (releaseObject.getJSONArray(ASSSETS_KEY) != null &&
                        releaseObject.getJSONArray(ASSSETS_KEY).length() > 0) {
                    JSONObject assetObject = (JSONObject) releaseObject.getJSONArray(ASSSETS_KEY).get(0);
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

    @Override
    public void append(String type, Object data) {
        if (type.equals(BaseBuilder.DATA_JSON)) {
            this._data = (String) data;
        }
    }
}
