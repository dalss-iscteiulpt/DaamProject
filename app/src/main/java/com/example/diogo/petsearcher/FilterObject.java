package com.example.diogo.petsearcher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Diogo on 24/04/2018.
 */

public class FilterObject implements Parcelable{
    String type;
    String Color;
    int distance;
    String gender;

    public FilterObject(String type, String color, int distance, String gender) {
        this.type = type;
        Color = color;
        this.distance = distance;
        this.gender = gender;
    }

    protected FilterObject(Parcel in) {
        type = in.readString();
        Color = in.readString();
        distance = in.readInt();
        gender = in.readString();
    }

    public static final Creator<FilterObject> CREATOR = new Creator<FilterObject>() {
        @Override
        public FilterObject createFromParcel(Parcel in) {
            return new FilterObject(in);
        }

        @Override
        public FilterObject[] newArray(int size) {
            return new FilterObject[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(Color);
        parcel.writeInt(distance);
        parcel.writeString(gender);
    }

    @Override
    public String toString() {
        return "Obj "+type+" "+Color+" "+gender;
    }
}
