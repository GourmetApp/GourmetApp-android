package com.jugarte.gourmet.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.lib.R;
import com.jugarte.gourmet.tracker.Tracker;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

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

    public static boolean isEqualsVersion(String localVersionTagName, String serverVersionTagName) {
        return (localVersionTagName.equalsIgnoreCase(serverVersionTagName));
    }

    public static void showDialog(final AppCompatActivity activity, final LastVersion lastVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        String message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message = String.valueOf(Html.fromHtml(lastVersion.getChangelog(), FROM_HTML_MODE_COMPACT));
        } else {
            message = String.valueOf(Html.fromHtml(lastVersion.getChangelog()));
        }

        builder.setTitle(activity.getResources().getString(R.string.dialog_title_last_version) + " " + lastVersion.getNameTagVersion())
                .setMessage(message)
                .setPositiveButton(R.string.dialog_download_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Tracker.getInstance().sendUpgradeEvent("download");
                        openUrlInBrowser(activity, lastVersion.getUrlHomePage());

                    }
                })
                .setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Tracker.getInstance().sendUpgradeEvent("cancel");
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Tracker.getInstance().sendUpgradeEvent("cancel");
                        dialog.cancel();
                    }
                });

        builder.show();
        setShowDialog(lastVersion.getNameTagVersion(), activity.getApplicationContext());
    }

    private static void openUrlInBrowser(Context context, String url) {
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
