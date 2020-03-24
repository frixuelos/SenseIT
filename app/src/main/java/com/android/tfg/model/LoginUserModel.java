package com.android.tfg.model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class LoginUserModel implements Serializable {

    private String UID, name, mail;

    public LoginUserModel(){
        this.UID="";
        this.name="";
        this.mail="";

    }

    public LoginUserModel(FirebaseUser user){
        this.UID=user.getUid();
        this.name=user.getDisplayName();
        this.mail=user.getEmail();
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

    public boolean equals(LoginUserModel user){
        return this.UID.equals(user.getUID()) && this.mail.equals(user.getMail()) && this.name.equals(user.getName());
    }

}
