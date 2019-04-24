package com.corey.ole;

import android.graphics.Bitmap;

import java.util.Date;

// custom class made for storing a tenant object.
public class Tenant {

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




    public Tenant(){

    }

    Tenant(String name, String gender, Date birthdate, int phone, String email, Bitmap photo, int property, String room) {
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

    // returns a string indicating how long ago this post was made
    protected String elapsedTimeString() {
        long diff = new Date().getTime() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        int daysInt = Math.round(days);
        int hoursInt = Math.round(hours);
        int minutesInt = Math.round(minutes);
        if (daysInt == 1) {
            return "1 day";
        } else if (daysInt > 1) {
            return Integer.toString(daysInt) + " days";
        } else if (hoursInt == 1) {
            return "1 hour";
        } else if (hoursInt > 1) {
            return Integer.toString(hoursInt) + " hours";
        } else {
            return "less than an hour";
        }
    }
}

