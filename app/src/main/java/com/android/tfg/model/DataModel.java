package com.android.tfg.model;

public class DataModel {

    private double temp;
    private double press;
    private double hum;
    private double light;

    public DataModel(){
        this.temp=0.0;
        this.press=0.0;
        this.hum=0.0;
        this.light=0.0;
    }

    public DataModel(double temp, double press, double hum, double light){
        this.temp=temp;
        this.press=press;
        this.hum=hum;
        this.light=light;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPress() {
        return press;
    }

    public void setPress(double press) {
        this.press = press;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

}
