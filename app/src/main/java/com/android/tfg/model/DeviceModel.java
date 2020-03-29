package com.android.tfg.model;

import com.google.android.gms.maps.model.LatLng;

public class DeviceModel {

    private String deviceID, deviceType, name;
    private LatLng site;

    public DeviceModel(){
        this.deviceID="";
        this.deviceType="";
        this.name="";
        this.site=new LatLng(0,0);
    }

    public DeviceModel(String deviceID, String deviceType, String name, LatLng site){
        this.deviceID=deviceID;
        this.deviceType=deviceType;
        this.name=name;
        this.site=site;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getSite() {
        return site;
    }

    public void setSite(LatLng site) {
        this.site = site;
    }
}
