package com.jugarte.gourmet.datamanagers;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.builders.LastVersionBuilder;
import com.jugarte.gourmet.internal.Constants;

import java.util.HashMap;

/**
 * Created by javiergon on 15/05/15.
 */
public class DataManagerFake {

    public Gourmet login(HashMap<String, Object> params) {
        Gourmet gourmet = new Gourmet();
        gourmet.currentBalance  = "145,45";

        Operation operation = null;
        for (int i = 0; i < 10; i++) {
            operation = new Operation();
            operation.name = "Bar restaurante USE";
            operation.price = "12";
            operation.date = "15/05/2015";
            operation.hour = "20:00";
            gourmet.operations.add(operation);
        }
        return gourmet;
    }

    public LastVersion getLastPublishVersion() {
        LastVersion lastVersion = new LastVersion();

        lastVersion.idVersion = "1603617";
        lastVersion.nameTagVersion = "v1.0.2";
        lastVersion.nameVersion = "Release v1.0.2";
        lastVersion.idDownload = "753792";
        lastVersion.nameDownload = "GourmetApp-v1.0.1.apk";
        lastVersion.urlDownload = "https://github.com/javierugarte/GourmetApp-android/releases/download/v1.0.1/GourmetApp-v1.0.1.apk";
        lastVersion.changelog = "* Spanish translation<br>* Exit login when the user has changed the password<br>* Fixed fonts of EditText";

        return lastVersion;
    }

}
