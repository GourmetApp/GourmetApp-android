package com.jugarte.gourmet.datamanagers;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

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
}
