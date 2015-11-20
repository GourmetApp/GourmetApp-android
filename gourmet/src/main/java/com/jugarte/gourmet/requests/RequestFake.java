package com.jugarte.gourmet.requests;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.beans.Operation;

/**
 * Created by javiergon on 15/05/15.
 */
public class RequestFake {

    public Gourmet login(String user, String pass) {
        Gourmet gourmet = new Gourmet();
        gourmet.currentBalance  = "83,45";
        gourmet.cardNumber = "0000004815162342";
        gourmet.errorCode = "0";

        String [] names = {"Central Perk", "Los Pollos Hermanos", "Moe's Bar", "MacLaren's Pub", "Bada Bing", "Casi K No", "La Almeja Borracha"};
        String [] prices = {"11,23", "5,8", "13,21", "34,55", "89,14", "23,33", "37,7"};
        String [] dates  = {"22/09/1994", "20/01/2008", "17/12/1989", "19/09/2005", "10/01/1999", "17/01/1999", "31/01/1999"};
        String [] hours  = {"22:00", "14:22", "21:56", "03:20", "20:45", "21:30", "22:50"};

        Operation operation = null;
        for (int i = 0; i < names.length; i++) {
            operation = new Operation();
            operation.name = names[i];
            operation.price = prices[i];
            operation.date = dates[i];
            operation.hour = hours[i];
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
