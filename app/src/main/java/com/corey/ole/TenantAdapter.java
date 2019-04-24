package com.corey.ole;

import android.graphics.Bitmap;


/* CardView for the tenant list under a property page*/

public class PropertyTenantView {
    private String name;
    private String address;
    private int[] tenant_ids;
    private Bitmap image;

    public void PropertyTenantView(String name, String address, int[] tenant_ids) {
        this.name = name;
        this.address = address;
        this.tenant_ids = tenant_ids;
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
