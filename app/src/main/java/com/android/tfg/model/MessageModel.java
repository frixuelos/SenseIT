package com.android.tfg.model;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class MessageModel {

    private double temp;
    private double pres;
    private double hum;
    private double batt;
    private double uv;
    private long seqNumber;
    private Timestamp date;
    private String lqi;


    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    private String operatorName;

    public long getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Object seqNumber) {
        if(seqNumber instanceof String){
            this.seqNumber = Long.parseLong((String)seqNumber);
        }else if(seqNumber instanceof Long){
            this.seqNumber = (long)seqNumber;
        }
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getLqi() {
        return lqi;
    }

    public void setLqi(String lqi) {
        this.lqi = lqi;
    }

    @NonNull
    @Override
    public String toString() {
        return this.temp + " ºC\t\t" + this.pres + " hPa\t\t" + this.hum+" %";
    }
}
