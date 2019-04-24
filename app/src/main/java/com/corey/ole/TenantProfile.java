package com.corey.ole;

import android.graphics.Bitmap;

import java.util.Date;

// custom class made for storing a tenant object.
public class TenantProfile {

    private static int global_id = 0;

    private int id;
    private String name;
    private String gender;
    private Date birthdate;
    private int phone;
    private String email;
    private Bitmap photo;
    private int property;
    private String room;




    public TenantProfile(){

    }

    TenantProfile(String name, String gender, Date birthdate, int phone, String email, Bitmap photo, int property, String room) {
        this.id = global_id;
        global_id += 1;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phone = phone;
        this.email = email;
        this.photo = photo;

        this.property = property;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public int getProperty() {
        return property;
    }

    public String getRoom() {
        return room;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

