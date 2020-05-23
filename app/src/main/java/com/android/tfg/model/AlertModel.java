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
    private boolean minTemp, minHum, minPres, minUv, maxTemp, maxHum, maxPres, maxUv;
    private double minTempValue, maxTempValue, minHumValue, maxHumValue, minPresValue, maxPresValue, minUuValue, maxUuValue;

    public AlertModel(@NonNull String deviceID, String deviceName, boolean minTemp, boolean minHum, boolean minPres, boolean minUv, boolean maxTemp, boolean maxHum, boolean maxPres, boolean maxUv, double minTempValue, double maxTempValue, double minHumValue, double maxHumValue, double minPresValue, double maxPresValue, double minUuValue, double maxUuValue) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.minTemp = minTemp;
        this.minHum = minHum;
        this.minPres = minPres;
        this.minUv = minUv;
        this.maxTemp = maxTemp;
        this.maxHum = maxHum;
        this.maxPres = maxPres;
        this.maxUv = maxUv;
        this.minTempValue = minTempValue;
        this.maxTempValue = maxTempValue;
        this.minHumValue = minHumValue;
        this.maxHumValue = maxHumValue;
        this.minPresValue = minPresValue;
        this.maxPresValue = maxPresValue;
        this.minUuValue = minUuValue;
        this.maxUuValue = maxUuValue;
    }

    @NonNull
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(@NonNull String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isMinTemp() {
        return minTemp;
    }

    public void setMinTemp(boolean minTemp) {
        this.minTemp = minTemp;
    }

    public boolean isMinHum() {
        return minHum;
    }

    public void setMinHum(boolean minHum) {
        this.minHum = minHum;
    }

    public boolean isMinPres() {
        return minPres;
    }

    public void setMinPres(boolean minPres) {
        this.minPres = minPres;
    }

    public boolean isMinUv() {
        return minUv;
    }

    public void setMinUv(boolean minUv) {
        this.minUv = minUv;
    }

    public boolean isMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(boolean maxTemp) {
        this.maxTemp = maxTemp;
    }

    public boolean isMaxHum() {
        return maxHum;
    }

    public void setMaxHum(boolean maxHum) {
        this.maxHum = maxHum;
    }

    public boolean isMaxPres() {
        return maxPres;
    }

    public void setMaxPres(boolean maxPres) {
        this.maxPres = maxPres;
    }

    public boolean isMaxUv() {
        return maxUv;
    }

    public void setMaxUv(boolean maxUv) {
        this.maxUv = maxUv;
    }

    public double getMinTempValue() {
        return minTempValue;
    }

    public void setMinTempValue(double minTempValue) {
        this.minTempValue = minTempValue;
    }

    public double getMaxTempValue() {
        return maxTempValue;
    }

    public void setMaxTempValue(double maxTempValue) {
        this.maxTempValue = maxTempValue;
    }

    public double getMinHumValue() {
        return minHumValue;
    }

    public void setMinHumValue(double minHumValue) {
        this.minHumValue = minHumValue;
    }

    public double getMaxHumValue() {
        return maxHumValue;
    }

    public void setMaxHumValue(double maxHumValue) {
        this.maxHumValue = maxHumValue;
    }

    public double getMinPresValue() {
        return minPresValue;
    }

    public void setMinPresValue(double minPresValue) {
        this.minPresValue = minPresValue;
    }

    public double getMaxPresValue() {
        return maxPresValue;
    }

    public void setMaxPresValue(double maxPresValue) {
        this.maxPresValue = maxPresValue;
    }

    public double getMinUuValue() {
        return minUuValue;
    }

    public void setMinUuValue(double minUuValue) {
        this.minUuValue = minUuValue;
    }

    public double getMaxUuValue() {
        return maxUuValue;
    }

    public void setMaxUuValue(double maxUuValue) {
        this.maxUuValue = maxUuValue;
    }
}
