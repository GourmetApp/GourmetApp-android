package com.jugarte.gourmet.domine.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gourmet implements Parcelable {

    public Gourmet() {
    }

    private String cardNumber = null;
    private String currentBalance = null;
    private String modificationDate = null;
    private ArrayList<Operation> operations = null;

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

    public List<Operation> getOperations() {
        return operations;
    }

    public ArrayList<Operation> getOperations(String pattern) {
        ArrayList<Operation> copyOperations = new ArrayList<>();

        for (Operation operation : operations) {
            if (operation.getName().toLowerCase().contains(pattern.toLowerCase())) {
                copyOperations.add(operation);
            }
        }
        return copyOperations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = (ArrayList<Operation>) operations;
    }

    public void addOperation(Operation operation) {
        if (operations == null) {
            operations = new ArrayList<>();
        }

        this.operations.add(operation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardNumber);
        dest.writeString(this.currentBalance);
        dest.writeString(this.modificationDate);
        dest.writeList(this.operations);
    }

    protected Gourmet(Parcel in) {
        this.cardNumber = in.readString();
        this.currentBalance = in.readString();
        this.modificationDate = in.readString();
        this.operations = new ArrayList<>();
        in.readList(this.operations, Operation.class.getClassLoader());
    }

    public static final Parcelable.Creator<Gourmet> CREATOR = new Parcelable.Creator<Gourmet>() {
        @Override
        public Gourmet createFromParcel(Parcel source) {
            return new Gourmet(source);
        }

        @Override
        public Gourmet[] newArray(int size) {
            return new Gourmet[size];
        }
    };

    public void orderOperations() {
        Collections.sort(operations, Collections.<Operation>reverseOrder());
    }
}
