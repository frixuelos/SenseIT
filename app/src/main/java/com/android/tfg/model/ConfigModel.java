package com.android.tfg.model;

public class ConfigModel {

    private int sleepTime;
    private int downlinkFreq;

    public ConfigModel(){
    }

    public ConfigModel(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getDownlinkFreq() {
        return downlinkFreq;
    }

    public void setDownlinkFreq(int downlinkFreq) {
        this.downlinkFreq = downlinkFreq;
    }

}
