package com.jugarte.gourmet.beans;

import java.util.ArrayList;

/**
 * Created by javiergon on 18/05/15.
 */
public class Gourmet {

    public String currentBalance = null;

    public String errorCode = null;

    public ArrayList<Operation> operations = null;

    public Gourmet() {
        operations = new ArrayList<Operation>();
    }
}
