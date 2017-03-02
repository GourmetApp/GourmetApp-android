package com.jugarte.gourmet.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Operation implements Parcelable {

    public Operation() {
    }

    private String name = null;
    private String price = null;
    private String date = null;
    private String hour = null;

    public String getId() {
        return date + hour;
    }

    public String getName() {
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

    protected Operation(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.date = in.readString();
        this.hour = in.readString();
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

}
