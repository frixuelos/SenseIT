package com.android.tfg.model;

import android.util.Patterns;

import java.io.Serializable;

public class LoginUserModel implements Serializable {

    private String UID, name, mail;

    public LoginUserModel(){
        this.UID="";
        this.name="";
        this.mail="";

    }

    public LoginUserModel(String email, String passwd){
        this.mail=email;
    }

    public LoginUserModel(String UID, String name, String mail) {
        this.UID = UID;
        this.name = name;
        this.mail = mail;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
