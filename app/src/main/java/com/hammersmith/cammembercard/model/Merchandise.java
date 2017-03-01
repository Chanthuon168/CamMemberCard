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
    @SerializedName("contact")
    private String contact;
    @SerializedName("num_card")
    private int numCard;
    @SerializedName("num_scan")
    private int numScan;
    @SerializedName("point")
    private int point;
    @SerializedName("rating")
    private String rating;
    @SerializedName("num_using")
    private int numUsing;
    @SerializedName("user_scan")
    private int userScan;
    @SerializedName("mem_id")
    private String memId;
    @SerializedName("country")
    private String country;
    @SerializedName("gender")
    private String gender;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @SerializedName("mer_email")
    private String merEmail;
    @SerializedName("mer_contact")
    private String merContact;
    @SerializedName("mer_contact_alternate")
    private String merContactAlternate;
    @SerializedName("mer_address")
    private String merAddress;
    @SerializedName("website")
    private String website;

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getNumCard() {
        return numCard;
    }

    public void setNumCard(int numCard) {
        this.numCard = numCard;
    }

    public int getNumScan() {
        return numScan;
    }

    public void setNumScan(int numScan) {
        this.numScan = numScan;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMerEmail() {
        return merEmail;
    }

    public void setMerEmail(String merEmail) {
        this.merEmail = merEmail;
    }

    public String getMerContact() {
        return merContact;
    }

    public void setMerContact(String merContact) {
        this.merContact = merContact;
    }

    public String getMerContactAlternate() {
        return merContactAlternate;
    }

    public void setMerContactAlternate(String merContactAlternate) {
        this.merContactAlternate = merContactAlternate;
    }

    public String getMerAddress() {
        return merAddress;
    }

    public void setMerAddress(String merAddress) {
        this.merAddress = merAddress;
    }

    public int getNumUsing() {
        return numUsing;
    }

    public void setNumUsing(int numUsing) {
        this.numUsing = numUsing;
    }

    public int getUserScan() {
        return userScan;
    }

    public void setUserScan(int userScan) {
        this.userScan = userScan;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
