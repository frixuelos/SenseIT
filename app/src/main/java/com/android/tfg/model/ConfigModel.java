package com.android.tfg.model;

public class ConfigModel {

    private int sleepTime;

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

}
