package com.jugarte.gourmet.domine.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gourmet implements Parcelable {

    private String cardNumber = null;
    private String currentBalance = null;
    private String modificationDate = null;
    private int newOperations;
    private List<Operation> operations = null;
    private boolean increaseOfBalance;

    public Gourmet() {
    }

    public Gourmet(Parcel in) {
        this.cardNumber = in.readString();
        this.currentBalance = in.readString();
        this.modificationDate = in.readString();
        this.newOperations = in.readInt();
        this.increaseOfBalance = in.readInt() == 1;
        this.operations = new ArrayList<>();
        in.readList(this.operations, Operation.class.getClassLoader());
    }

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

    @Exclude
    public int getNewOperations() {
        return newOperations;
    }

    public void setNewOperations(int newOperations) {
        this.newOperations = newOperations;
    }

    @Exclude
    public boolean isIncreaseOfBalance() {
        return increaseOfBalance;
    }

    public void setIncreaseOfBalance(boolean increaseOfBalance) {
        this.increaseOfBalance = increaseOfBalance;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public List<Operation> getOperations(String pattern) {
        List<Operation> copyOperations = new ArrayList<>();

        for (Operation operation : operations) {
            if (operation.getName().toLowerCase().contains(pattern.toLowerCase())) {
                copyOperations.add(operation);
            }
        }
        return copyOperations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
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
        dest.writeInt(this.newOperations);
        dest.writeInt(this.increaseOfBalance ? 1 : 0);
        dest.writeList(this.operations);
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
