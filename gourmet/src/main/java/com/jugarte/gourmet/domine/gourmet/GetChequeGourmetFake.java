package com.jugarte.gourmet.domine.gourmet;

import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.domine.beans.Operation;

import java.util.ArrayList;
import java.util.List;

class GetChequeGourmetFake implements GetChequeGourmet {

    @Override
    public void execute(String user, String password, OnChequeGourmetResponse response) {

        List<Operation> operationList = new ArrayList<>();

        operationList.add(new Operation("Actualización de saldo", "180", "16/11/2017", "12:14"));
        operationList.add(new Operation("Gasto 1", "30", "15/11/2017", "12:14"));

        ChequeGourmet chequeGourmet = new ChequeGourmet("0000", "300", operationList);

        response.success(chequeGourmet);
    }
}
