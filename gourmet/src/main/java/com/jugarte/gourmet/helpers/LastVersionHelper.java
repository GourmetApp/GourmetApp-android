package com.jugarte.gourmet.helpers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.lib.R;


/**
 * Created by javiergon on 04/08/15.
 */
public class LastVersionHelper {

    private static final String PREFERENCE_ID = "last_version";

    private static final String PREFERENCE_KEY = "should_show";

    public static String getCurrentVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return "v" + pInfo.versionName;
    }

    public static boolean isEqualsVersion (String localVersionTagName, String serverVersionTagName){
        return (localVersionTagName.equalsIgnoreCase(serverVersionTagName));
    }

    public static void showDialog(final Activity activity, final LastVersion lastVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.dialog_title_last_version) + " " + lastVersion.nameTagVersion)
                .setMessage(Html.fromHtml(lastVersion.changelog))
                .setPositiveButton(R.string.dialog_download_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openUrlInBrowser(activity, lastVersion.urlHomePage);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();

        setShowDialog(lastVersion.nameTagVersion, activity.getApplicationContext());
    }

    public static void openUrlInBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    public static boolean shouldShowDialog(String version, Context context) {
        String oldVersion = getSharedPreferences(context).getString(PREFERENCE_KEY, "");

        return !oldVersion.equalsIgnoreCase(version);
    }

    private static void setShowDialog(String version, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_KEY, version);
        editor.apply();
    }
}
