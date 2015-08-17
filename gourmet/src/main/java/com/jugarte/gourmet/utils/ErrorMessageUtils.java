package com.jugarte.gourmet.utils;

import android.content.Context;

import com.jugarte.gourmet.lib.R;

/**
 * Created by javiergon on 17/08/15.
 */
public class ErrorMessageUtils {

    public static String getErrorMessageWithCode(Context context, String errorCode) {
        int errorCodeResource = 0;

        if (errorCode.equalsIgnoreCase("1")) {
            errorCodeResource = R.string.error_not_user_or_pass_code1;
        } else if (errorCode.equalsIgnoreCase("2")) {
            errorCodeResource = R.string.error_user_or_password_incorrect_code2;
        } else if (errorCode.equalsIgnoreCase("3")) {
            errorCodeResource = R.string.error_connection_code3;
        } else {
            return "";
        }
        return context.getResources().getString(errorCodeResource);
    }
}
