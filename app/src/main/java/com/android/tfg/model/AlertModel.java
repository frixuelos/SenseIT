package com.android.tfg.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(tableName = "alerts")
public class AlertModel implements Serializable {

    @PrimaryKey
    @NonNull
    private String deviceID;
    private String deviceName;
    private boolean temp, hum, pres, uv;
    private double minTemp, minHum, minPres, minUv;
    private double maxTemp, maxHum, maxPres, maxUv;

    public AlertModel(@NotNull String deviceID, String deviceName, boolean temp, boolean hum, boolean pres, boolean uv, double minTemp, double minHum, double minPres, double minUv, double maxTemp, double maxHum, double maxPres, double maxUv) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.temp = temp;
        this.hum = hum;
        this.pres = pres;
        this.uv = uv;
        this.minTemp = minTemp;
        this.minHum = minHum;
        this.minPres = minPres;
        this.minUv = minUv;
        this.maxTemp = maxTemp;
        this.maxHum = maxHum;
        this.maxPres = maxPres;
        this.maxUv = maxUv;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public boolean isHum() {
        return hum;
    }

    public void setHum(boolean hum) {
        this.hum = hum;
    }

    public boolean isPres() {
        return pres;
    }

    public void setPres(boolean pres) {
        this.pres = pres;
    }

    public boolean isUv() {
        return uv;
    }

    public void setUv(boolean uv) {
        this.uv = uv;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMinHum() {
        return minHum;
    }

    public void setMinHum(double minHum) {
        this.minHum = minHum;
    }

    public double getMinPres() {
        return minPres;
    }

    public void setMinPres(double minPres) {
        this.minPres = minPres;
    }

    public double getMinUv() {
        return minUv;
    }

    public void setMinUv(double minUv) {
        this.minUv = minUv;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMaxHum() {
        return maxHum;
    }

    public void setMaxHum(double maxHum) {
        this.maxHum = maxHum;
    }

    public double getMaxPres() {
        return maxPres;
    }

    public void setMaxPres(double maxPres) {
        this.maxPres = maxPres;
    }

    public double getMaxUv() {
        return maxUv;
    }

    public void setMaxUv(double maxUv) {
        this.maxUv = maxUv;
    }
}
