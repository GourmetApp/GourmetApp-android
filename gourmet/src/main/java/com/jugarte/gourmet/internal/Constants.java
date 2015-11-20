package com.jugarte.gourmet.internal;

/**
 * Created by javiergon on 06/05/15.
 */
public class Constants {

    /// PARAMS
    public static boolean FAKE_SERVICES = true;

    public static String SERVICE_PARAM_USER_KEY = "usuario";
    public static String SERVICE_PARAM_PASS_KEY = "contrasena";
    public static String SERVICE_PARAM_TOKEN_KEY = "token";
    public static String SERVICE_PARAM_TOKEN_RESPONSE = "xAeSYsTQQTCVyPOGWLpR";

    public static String getUrlLastPublishVersion() {
        return "https://api.github.com/repos/javierugarte/GourmetApp-android/releases";
    }

    public static String getUrlLoginService() {
        //return "http://jugarte.es/api/gourmet/login.php";
        return "http://tarjetagourmet.chequegourmet.com/processLogin_iphoneApp.jsp";
    }

    public static String getUrlHomePage() {
        return "http://javierugarte.github.io/GourmetApp-android";
    }

}
