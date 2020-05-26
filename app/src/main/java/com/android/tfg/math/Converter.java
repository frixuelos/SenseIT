package com.android.tfg.math;

public class Converter {

    public static double toCelsius(double temp){
        return Math.round((temp - 32.0) * 5.0 / 9.0 * 100.0) / 100.0;
    }

    public static double toFarenheit(double temp){
        return Math.round((9 * temp / 5 + 32.0) * 100.0) / 100.0;
    }

    public static double toHPA(double pres){
        return Math.round(pres / 0.000987 * 100.0) / 100.0;
    }

    public static double toATM(double pres){
        return Math.round(0.000987 * pres * 100.0) / 100.0;
    }

    public static double toMW(double uv){
        return Math.round(uv / 10.0 * 100.0) / 100.0;
    }

    public static double toW(double uv){
        return Math.round(uv * 10.0 * 100.0) / 100.0;
    }
}
