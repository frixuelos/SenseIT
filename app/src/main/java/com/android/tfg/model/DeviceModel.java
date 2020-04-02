package com.android.tfg.model;

import com.google.android.gms.maps.model.LatLng;

public class DeviceModel {

    private String deviceID, deviceType, name;
    private MessageModel lastMessage;

    public DeviceModel(String deviceID, String deviceType, String name, MessageModel lastMessage){
        this.deviceID=deviceID;
        this.deviceType=deviceType;
        this.name=name;
        this.lastMessage=lastMessage;
    }

    public LatLng getSite(){
        return new LatLng(this.lastMessage.getComputedLocation().getLat(), this.lastMessage.getComputedLocation().getLng());
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        if(name!=null){
            return name;
        }
        return deviceID;
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
