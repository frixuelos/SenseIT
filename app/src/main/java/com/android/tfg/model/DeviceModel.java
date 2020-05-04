package com.android.tfg.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class DeviceModel {

    @Exclude // Excluir el ID para el update
    private String id;

    private String type, name;
    private ComputedLocationModel computedLocation;
    private MessageModel lastMessage;

    public DeviceModel(){}

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


}
