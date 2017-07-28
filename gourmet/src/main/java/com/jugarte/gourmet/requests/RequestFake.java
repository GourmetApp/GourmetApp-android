package com.jugarte.gourmet.requests;

import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.domine.beans.Operation;
import com.jugarte.gourmet.internal.Constants;

import java.util.ArrayList;
import java.util.List;

public class RequestFake {

    public Gourmet login() {
        Gourmet gourmet = new Gourmet();
        gourmet.setCurrentBalance("83,45");
        gourmet.setCardNumber("0000004815162342");

        String[] names = {"Central Perk", "Los Pollos Hermanos", "Moe's Bar", "MacLaren's Pub", "Bada Bing", "Casi K No", "La Almeja Borracha"};
        String[] prices = {"11,23", "5,8", "13,21", "34,55", "89,14", "23,33", "37,7"};
        String[] dates = {"22/09/1994", "20/01/2008", "17/12/1989", "19/09/2005", "10/01/1999", "17/01/1999", "31/01/1999"};
        String[] hours = {"22:00", "14:22", "21:56", "03:20", "20:45", "21:30", "22:50"};

        Operation operation;
        List<Operation> operations = new ArrayList<>(names.length);
        for (int i = 0; i < names.length; i++) {
            operation = new Operation();
            operation.setName(names[i]);
            operation.setPrice(prices[i]);
            operation.setDate(dates[i]);
            operation.setHour(hours[i]);
            operations.add(operation);
        }
        gourmet.setOperations(operations);

        return gourmet;
    }

    public LastVersion getLastPublishVersion() {
        LastVersion lastVersion = new LastVersion();

        lastVersion.setNameTagVersion("v1.4.1");
        lastVersion.setNameVersion("Release v1.4.1");
        lastVersion.setNameDownload("GourmetApp-v1.0.1.apk");
        lastVersion.setUrlHomePage(Constants.getUrlHomePage());
        lastVersion.setUrlDownload("https://github.com/javierugarte/GourmetApp-android/releases/download/v1.0.1/GourmetApp-v1.0.1.apk");
        lastVersion.setChangelog("* Spanish translation<br>* Exit login when the user has changed the password<br>* Fixed fonts of EditText");

        return lastVersion;
    }

}
