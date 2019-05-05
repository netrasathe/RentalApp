package com.corey.ole;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

// custom class made for storing a tenant object.
public class TenantProfile {

    public static final String EXTRA_TENANT_ID = "tenantID";
    public static final String EXTRA_LABEL = "label";

    private String id;
    private String name;
    private String gender;
    private Date birthdate;
    private int phone;
    private String email;
    private Bitmap photo;
    private int propertyID;
    private String room;


    public TenantProfile(String id, String name, String gender, Date birthdate, int phone, String email, Bitmap photo, int propertyID, String room) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phone = phone;
        this.email = email;
        this.photo = photo;

        this.propertyID = propertyID;
        this.room = room;
    }

    public String getId() {
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

    public int getPropertyID() {
        return propertyID;
    }

    public String getRoom() {
        return room;
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

    public void setPropertyID(int property) {
        this.propertyID = property;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public static TenantProfile makeTestTenants(int propertyID) {
        TenantProfile tenant = new TenantProfile("1", "John Smith", "Male",
                new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)),
                1234567890,
                "email@email.com",
                null,
                propertyID,
                "212");
        return tenant;
    }

}


