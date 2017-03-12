package com.jugarte.gourmet.data.chequegourmet;

import com.jugarte.gourmet.beans.Operation;

import java.util.List;

public class ChequeGourmet {

    private String balance;
    private List<Operation> operations;

    public ChequeGourmet(String balance, List<Operation> operations) {
        this.balance = balance;
        this.operations = operations;
    }

    public String getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }

}
