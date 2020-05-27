package com.android.tfg.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class DeviceModel {

    @Exclude // Excluir el ID para el update
    private String id;

    private boolean alive;
    private String type, name;
    private double fixedLat, fixedLng;
    private int countryCode;
    private ComputedLocationModel computedLocation;
    private MessageModel lastMessage;
    private ConfigModel config;

    public DeviceModel(){}

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
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

    public ComputedLocationModel getComputedLocation() {
        return computedLocation;
    }

    public void setComputedLocation(ComputedLocationModel computedLocation) {
        this.computedLocation = computedLocation;
    }

    public LatLng getSite(){
        return new LatLng(this.computedLocation.getLat(), this.computedLocation.getLng());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageModel getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageModel lastMessage) {
        this.lastMessage = lastMessage;
    }


    public ConfigModel getConfig() {
        return config;
    }

    public void setConfig(ConfigModel config) {
        this.config = config;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
