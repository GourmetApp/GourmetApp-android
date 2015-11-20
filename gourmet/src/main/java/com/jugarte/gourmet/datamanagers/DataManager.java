package com.jugarte.gourmet.datamanagers;

import android.content.Context;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.builders.GourmetBuilder;
import com.jugarte.gourmet.builders.GourmetInternalBuilder;
import com.jugarte.gourmet.builders.LastVersionBuilder;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.DateHelper;
import com.jugarte.gourmet.helpers.RequestURLConnection;
import com.jugarte.gourmet.utils.LogUtils;
import com.jugarte.gourmet.internal.Constants;
import java.util.HashMap;

/**
 * Created by javiergon on 15/05/15.
 */
public class DataManager {

    private final Context context;

    public DataManager(Context context) {
        this.context = context;
    }

    public Gourmet login(String user, String pass) {
        if (user != null && pass != null) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Constants.SERVICE_PARAM_USER_KEY, user);
            params.put(Constants.SERVICE_PARAM_PASS_KEY, pass);
            params.put(Constants.SERVICE_PARAM_TOKEN_KEY, Constants.SERVICE_PARAM_TOKEN_RESPONSE);
            return this.internalLogin(params);
        }
        return null;
    }

    @Deprecated
    private Gourmet login(HashMap<String, String> params) {
        Gourmet gourmet = null;
        String response = RequestURLConnection.launchPostUrl(Constants.getUrlLoginService(), params);

        GourmetBuilder gourmetBuilder = new GourmetBuilder(context);
        gourmetBuilder.append(GourmetBuilder.DATA_JSON, response);

        try {
            gourmet = gourmetBuilder.build();
        } catch (Exception e) {
            LogUtils.LOGE("GOURMET", e.getMessage(), e);
        }

        if (gourmet == null) {
            return gourmetBuilder.getGourmetCacheData();
        }

        return gourmetBuilder.updateGourmetDataWithCache(gourmet);
    }

    private Gourmet internalLogin(HashMap<String, String> params) {
        Gourmet gourmet = null;
        String response = RequestURLConnection.launchPostUrl(Constants.getUrlLoginService(), params);

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(this.context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, response);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, CredentialsLogin.getUserCredential());
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, DateHelper.getCurrentDateTime());

        try {
            gourmet = gourmetBuilder.build();
        } catch (Exception e) {
            LogUtils.LOGE("GOURMET", e.getMessage(), e);
        }

        if (gourmet == null) {
            return gourmetBuilder.getGourmetCacheData();
        }

        if (gourmet.operations == null) {
            return gourmetBuilder.getGourmetCacheData();
        }

        return gourmetBuilder.updateGourmetDataWithCache(gourmet);
    }

}