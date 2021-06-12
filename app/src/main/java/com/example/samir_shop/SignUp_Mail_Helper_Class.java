package com.example.samir_shop;

public class SignUp_Mail_Helper_Class {

    String Name,Email,uid,imageUrl;

    public SignUp_Mail_Helper_Class(String name, String email, String uid, String imageUrl) {
        Name = name;
        Email = email;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public SignUp_Mail_Helper_Class() {


    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
