package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 9/9/2016.
 */
public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("mem_id")
    private String memId;
    @SerializedName("loginAs")
    private String loginAs;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("social_link")
    private String socialLink;
    @SerializedName("social_type")
    private String socialType;
    @SerializedName("photo")
    private String photo;
    @SerializedName("msg")
    private String msg;
    @SerializedName("password")
    private String password;
    @SerializedName("country")
    private String country;
    @SerializedName("gender")
    private String gender;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @SerializedName("address")
    private String address;
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

    public User() {
    }

    public User(String socialLink) {
        this.socialLink = socialLink;
    }

    public User(String name, String email, String photo, String socialLink, String socialType) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.socialLink = socialLink;
        this.socialType = socialType;
    }

    public User(String name, String email, String password, String photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public User(String loginAs, String email, String password) {
        this.loginAs = loginAs;
        this.email = email;
        this.password = password;
    }

    public User(String socialLink, String name, String email, String address, String contact, String photo) {
        this.socialLink = socialLink;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.photo = photo;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return contact;
    }

    public void setPhone(String phone) {
        this.contact = phone;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getLoginAs() {
        return loginAs;
    }

    public void setLoginAs(String loginAs) {
        this.loginAs = loginAs;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
