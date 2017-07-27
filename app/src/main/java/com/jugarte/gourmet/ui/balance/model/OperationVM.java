package com.jugarte.gourmet.ui.balance.model;

public class OperationVM {

    private String title;
    private String price;
    private String date;
    private String month;
    private boolean positive;

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

    public boolean isPositive() {
        return positive;
    }

    public static class Builder {
        private String title;
        private String price;
        private String date;
        private String month;
        private boolean positive;

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

        public Builder setPositive(boolean positive) {
            this.positive = positive;
            return this;
        }

        public OperationVM build() {
            OperationVM operationVM = new OperationVM();
            operationVM.title = title;
            operationVM.price = price;
            operationVM.date = date;
            operationVM.month = month;
            operationVM.positive = positive;

            return operationVM;
        }

    }

}
