package com.corey.ole;

import java.util.Date;
import java.util.UUID;

public class Repair {

    private Date date;
    private String id;
    private String request;
    private boolean isFixed;



    public Repair(String request, Date date, boolean isFixed) {
        this.id = UUID.randomUUID().toString();
        this.request = request;
        this.date = date;
        this.isFixed = isFixed;
    }

    public Repair() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}
