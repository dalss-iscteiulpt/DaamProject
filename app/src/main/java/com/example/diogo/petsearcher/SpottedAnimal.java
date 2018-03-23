package com.example.diogo.petsearcher;

import android.media.Image;

/**
 * Created by Diogo on 20/03/2018.
 */

public class SpottedAnimal {

    protected int id;
    protected String type;
    protected  String breed;
    protected String primaryC;
    protected String secodnaryC;
    protected String pattern;
    protected String spotttedDate;
    protected String spottedHour;
    protected String phoneNr;
    protected String email;
    protected String picture;
    protected String realLocation;
    protected String coordLocation;
    protected String commentaries;

    public SpottedAnimal(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
}
