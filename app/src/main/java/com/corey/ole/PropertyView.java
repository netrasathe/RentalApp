package com.corey.ole;

import android.graphics.Bitmap;


/* CardView for Properties in Landlord's Home */

public class PropertyView {
    private String title;
    private String address;
    private int tenants;
    private Bitmap image;

    public void PropertyView(String title, String address, int tenants, Bitmap image) {
        this.title = title;
        this.address = address;
        this.tenants = tenants;
        this.image = image;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public void setAddress(String a) {
        this.address = a;
    }

    public void setTenants(int t){
        this.tenants = t;
    }

    public void setImage(Bitmap i) {
        this.image = i;
    }


    public String getTitle(){
        return this.title;
    }

    public String getAddress(){
        return this.address;
    }

    public int getTenants(){
        return this.tenants;
    }

    public Bitmap getImage(){
        return this.image;
    }

}
