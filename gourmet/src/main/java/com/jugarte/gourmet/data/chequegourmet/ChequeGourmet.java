package com.jugarte.gourmet.data.chequegourmet;

import com.jugarte.gourmet.domine.beans.Operation;

import java.util.List;

public class ChequeGourmet {

    private String cardNumber;
    private String balance;
    private List<Operation> operations;

    public ChequeGourmet(String cardNumber, String balance, List<Operation> operations) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.operations = operations;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }

}
