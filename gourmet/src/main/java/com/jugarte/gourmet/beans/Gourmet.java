package com.jugarte.gourmet.beans;

import java.util.ArrayList;

/**
 * Created by javiergon on 18/05/15.
 */
public class Gourmet {

    private String cardNumber = null;
    private String currentBalance = null;
    private String modificationDate = null;
    private boolean offlineMode;
    private ArrayList<Operation> operations = null;

    private String errorCode = null;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public void addOperation(Operation operation) {
        if (operations == null) {
            operations = new ArrayList<>();
        }

        this.operations.add(operation);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
