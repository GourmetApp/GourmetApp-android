package com.jugarte.gourmet.internal;

import android.content.Context;

import com.jugarte.gourmet.lib.R;

/**
 * Created by javiergon on 06/05/15.
 */
public class Constants {

    /// PARAMS
    public static boolean FAKE_SERVICES = false;

    public static String SERVICE_PARAM_USER_KEY = "usuario";
    public static String SERVICE_PARAM_PASS_KEY = "contrasena";
    public static String SERVICE_PARAM_TOKEN_KEY = "token";
    public static String SERVICE_PARAM_TOKEN_RESPONSE = "xAeSYsTQQTCVyPOGWLpR";

    public static String getUrlLastPublishVersion() {
        return "https://api.github.com/repos/gourmetapp/GourmetApp-android/releases";
    }

    public static String getUrlLoginService() {
        return "http://tarjetagourmet.chequegourmet.com/processLogin_iphoneApp.jsp";
    }

    public static String getUrlGitHubProject() {
        return "http://www.github.com/gourmetapp/GourmetApp-android";
    }

    public static String getUrlHomePage() {
        return "http://gourmetapp.github.io/android";
    }

    public static String getShareText(Context context) {
        return String.format(context.getResources().getString(R.string.share_text), getUrlHomePage());
    }

}
