package com.hammersmith.cammembercard.model;

import android.icu.text.SymbolTable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chan Thuon on 11/22/2016.
 */
public class MemberCard implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("md_id")
    private int merId;
    @SerializedName("img_card")
    private String ImgCard;
    @SerializedName("img_merchandise")
    private String imgMerchandise;
    @SerializedName("name")
    private String name;
    @SerializedName("rating")
    private String rating;
    @SerializedName("address")
    private String address;
    @SerializedName("exp")
    private String expDate;
    @SerializedName("status")
    private String status;
    @SerializedName("count")
    private String count;
    @SerializedName("social_link")
    private String socialLink;
    @SerializedName("size_status")
    private String sizeStats;
    @SerializedName("outlet")
    private String outlet;

    public MemberCard() {
    }

    public MemberCard(String socialLink) {
        this.socialLink = socialLink;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgMerchandise() {
        return imgMerchandise;
    }

    public void setImgMerchandise(String imgMerchandise) {
        this.imgMerchandise = imgMerchandise;
    }

    public String getImgCard() {
        return ImgCard;
    }

    public void setImgCard(String imgCard) {
        ImgCard = imgCard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getSizeStats() {
        return sizeStats;
    }

    public void setSizeStats(String sizeStats) {
        this.sizeStats = sizeStats;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }
}
