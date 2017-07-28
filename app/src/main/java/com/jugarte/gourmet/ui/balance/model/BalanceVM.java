package com.jugarte.gourmet.ui.balance.model;

import java.util.List;

public class BalanceVM {

    private String cardNumber;
    private String current;
    private List<Object> operations;

    private BalanceVM(String cardNumber, String current, List<Object> operations) {
        this.cardNumber = cardNumber;
        this.current = current;
        this.operations = operations;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCurrent() {
        return current;
    }

    public List<Object> getOperations() {
        return operations;
    }

    public static class Builder {

        private String cardNumber;
        private String current;
        private List<Object> operations;

        public Builder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder setCurrent(String current) {
            this.current = current;
            return this;
        }

        public Builder setOperations(List<Object> operations) {
            this.operations = operations;
            return this;
        }

        public BalanceVM build() {
            return new BalanceVM(cardNumber, current, operations);
        }
    }
}
