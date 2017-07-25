package com.jugarte.gourmet.ui.balance.model;

public class OperationVM {

    public enum State {
        POSITIVE,
        NEGATIVE
    }

    private String title;
    private String price;
    private String date;
    private String month;
    private State state;

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public State getState() {
        return state;
    }

    public static class Builder {
        private String title;
        private String price;
        private String date;
        private String month;
        private State state;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setMonth(String month) {
            this.month = month;
            return this;
        }

        public Builder setState(State state) {
            this.state = state;
            return this;
        }

        public OperationVM build() {
            OperationVM operationVM = new OperationVM();
            operationVM.title = title;
            operationVM.price = price;
            operationVM.date = date;
            operationVM.month = month;
            operationVM.state = state;

            return operationVM;
        }

    }

}
