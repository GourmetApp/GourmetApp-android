package com.jugarte.gourmet.domine;

import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmetDataManager;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

class GetChequeGourmet {

    interface OnChequeGourmetResponse {
        void success(ChequeGourmet chequeGourmet);
        void error(Exception exception);
    }

    void execute(String user, String password, OnChequeGourmetResponse response) {
        ChequeGourmetDataManager chequeGourmetDataManager = new ChequeGourmetDataManager();
        try {
            ChequeGourmet chequeGourmet = chequeGourmetDataManager.getChequeGourmet(user, password);
            response.success(chequeGourmet);
        } catch (ConnectionException | EmptyException | NotFoundException e) {
            response.error(e);
        }
    }
}
