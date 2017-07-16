package com.jugarte.gourmet.domine.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Operation implements Parcelable, Comparable<Operation> {

    private String name = null;
    private String price = null;
    private String date = null;
    private String hour = null;

    public Operation() {
    }

    public Operation(String name, String price, String date, String hour) {
        this.name = name;
        this.price= price;
        this.date = date;
        this.hour = hour;
    }

    private Operation(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.date = in.readString();
        this.hour = in.readString();
    }

    public String getId() {
        return date + hour;
    }

    public String getName() {
        fixTitle();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Exclude
    private Date getDateObject() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            return formatter.parse(date + " " + hour);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.date);
        dest.writeString(this.hour);
    }

    public static final Parcelable.Creator<Operation> CREATOR = new Parcelable.Creator<Operation>() {
        @Override
        public Operation createFromParcel(Parcel source) {
            return new Operation(source);
        }

        @Override
        public Operation[] newArray(int size) {
            return new Operation[size];
        }
    };

    @Override
    public int compareTo(@NonNull Operation operation) {
        if (getDateObject() != null && operation.getDateObject() != null) {
            return getDateObject().compareTo(operation.getDateObject());
        }
        return 0;
    }

    private void fixTitle() {
        name = name.replace("¥", "Ñ");
        name = name.replace("#", "Ñ");
        name = name.replace("ï", "'");
    }

}
