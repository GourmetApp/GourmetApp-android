package com.jugarte.gourmet.domine.gourmet;

import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;



public interface GetChequeGourmet {

    interface OnChequeGourmetResponse {
        void success(ChequeGourmet chequeGourmet);

        void error(Exception exception);
    }

    void execute(String user, String password, OnChequeGourmetResponse response);
}
