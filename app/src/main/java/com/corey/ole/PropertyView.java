package com.corey.ole;

import android.graphics.Bitmap;

import java.util.Date;


/* CardView for Properties in Landlord's Home */

public class PropertyView {
    private static int global_id = 0;

    private int id;
    private String title;
    private String address;
    private int tenants;
    private Bitmap image;

    private int[] tenantIDList;

    PropertyView(){

    }

    PropertyView(String title, String address, int tenants, Bitmap image) {
        this.id = global_id;
        global_id += 1;
        this.title = title;
        this.address = address;
        this.tenants = tenants;
        this.image = image;
        this.tenantIDList = new int[]{};
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


    public int getId(){
        return this.id;
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


    public static PropertyView makeTestProperty() {
        return new PropertyView(
                "TestProperty",
                "1111 Shattuck Ave.",
                12,
                null);
    }

}
