package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imac on 10/2/17.
 */
public class Merchandise {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String photo;
    @SerializedName("phone")
    private String phone;
    @SerializedName("phone_alternate")
    private String phoneAlternate;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String Address;
    @SerializedName("mer_photo")
    private String merPhoto;
    @SerializedName("mer_name")
    private String merName;
    @SerializedName("mer_id")
    private int merId;

    public Merchandise(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneAlternate() {
        return phoneAlternate;
    }

    public void setPhoneAlternate(String phoneAlternate) {
        this.phoneAlternate = phoneAlternate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMerPhoto() {
        return merPhoto;
    }

    public void setMerPhoto(String merPhoto) {
        this.merPhoto = merPhoto;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }
}
