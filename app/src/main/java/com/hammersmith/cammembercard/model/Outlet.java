package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class Outlet implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("mer_id")
    private int merId;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("address")
    private String address;
    @SerializedName("time_open")
    private String timeOpen;
    @SerializedName("time_close")
    private String timeClose;
    @SerializedName("contact")
    private String contact;
    @SerializedName("map")
    private String map;
    @SerializedName("facebook")
    private String facebook;
    @SerializedName("website")
    private String website;

    public Outlet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
