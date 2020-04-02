package com.android.tfg.model;

import androidx.annotation.NonNull;

public class MessageModel {

    private double temp, pres, hum, batt, fixedLat, fixedLng;
    private int date, countryCode;
    private String operatorName, lqi;
    private ComputedLocationModel computedLocation;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPres() {
        return pres;
    }

    public void setPres(double pres) {
        this.pres = pres;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public double getBatt() {
        return batt;
    }

    public void setBatt(double batt) {
        this.batt = batt;
    }

    public double getFixedLat() {
        return fixedLat;
    }

    public void setFixedLat(double fixedLat) {
        this.fixedLat = fixedLat;
    }

    public double getFixedLng() {
        return fixedLng;
    }

    public void setFixedLng(double fixedLng) {
        this.fixedLng = fixedLng;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getLqi() {
        return lqi;
    }

    public void setLqi(String lqi) {
        this.lqi = lqi;
    }

    public ComputedLocationModel getComputedLocation() {
        return computedLocation;
    }

    public void setComputedLocation(ComputedLocationModel computedLocation) {
        this.computedLocation = computedLocation;
    }

    @NonNull
    @Override
    public String toString() {
        return this.temp + "\n" + this.pres + "\n" + this.hum;
    }
}
