package com.example.diogo.petsearcher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Diogo on 20/03/2018.
 */

//Alt+Insert Getter and Setter
public class SpottedAnimal implements Parcelable{

    protected String id;
    protected String type;
    protected String breed;
    protected String primaryC;
    protected String secodnaryC;
    protected String gender;
    protected String size;
    protected String spotttedDate;
    protected String spottedHour;
    protected String phoneNr;
    protected String email;
    protected String pictureID;
    protected String realLocation;
    protected String coordLocation;
    protected String commentaries;

    public SpottedAnimal(){
    }

    public SpottedAnimal(Parcel in){
        id = in.readString();
        type = in.readString();
        breed = in.readString();
        primaryC = in.readString();
        secodnaryC = in.readString();
        gender = in.readString();
        size = in.readString();
        spotttedDate = in.readString();
        spottedHour = in.readString();
        phoneNr = in.readString();
        email = in.readString();
        pictureID = in.readString();
        realLocation = in.readString();
        coordLocation = in.readString();
        commentaries = in.readString();
    }

    public static final Creator<SpottedAnimal> CREATOR = new Creator<SpottedAnimal>() {
        @Override
        public SpottedAnimal createFromParcel(Parcel in) {
            return new SpottedAnimal(in);
        }

        @Override
        public SpottedAnimal[] newArray(int size) {
            return new SpottedAnimal[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrimaryC() {
        return primaryC;
    }

    public void setPrimaryC(String primaryC) {
        this.primaryC = primaryC;
    }

    public String getSecodnaryC() {
        return secodnaryC;
    }

    public void setSecodnaryC(String secodnaryC) {
        this.secodnaryC = secodnaryC;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpotttedDate() {
        return spotttedDate;
    }

    public void setSpotttedDate(String spotttedDate) {
        this.spotttedDate = spotttedDate;
    }

    public String getSpottedHour() {
        return spottedHour;
    }

    public void setSpottedHour(String spottedHour) {
        this.spottedHour = spottedHour;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    public String getRealLocation() {
        return realLocation;
    }

    public void setRealLocation(String realLocation) {
        this.realLocation = realLocation;
    }

    public String getCoordLocation() {
        return coordLocation;
    }

    public void setCoordLocation(String coordLocation) {
        this.coordLocation = coordLocation;
    }

    public String getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(String commentaries) {
        this.commentaries = commentaries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(breed);
        parcel.writeString(primaryC);
        parcel.writeString(secodnaryC);
        parcel.writeString(gender);
        parcel.writeString(size);
        parcel.writeString(spotttedDate);
        parcel.writeString(spottedHour);
        parcel.writeString(phoneNr);
        parcel.writeString(email);
        parcel.writeString(pictureID);
        parcel.writeString(realLocation);
        parcel.writeString(coordLocation);
        parcel.writeString(commentaries);
    }
}
