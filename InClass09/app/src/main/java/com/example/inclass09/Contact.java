package com.example.inclass09;

import android.graphics.Bitmap;
import java.io.Serializable;

public class Contact implements Serializable {

    String name, email, phone, id, imageBitmap;
    //Bitmap imageBitmap;

    public Contact(){

    }

    public Contact(String name, String email, String phone, String imageBitmap) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageBitmap = imageBitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String  getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(String imageBitmap) {
        this.imageBitmap = imageBitmap;
    }


}
