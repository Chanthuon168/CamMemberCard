package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by imac on 28/3/17.
 */
public class Promotion implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("mer_id")
    private int merId;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String merName;
    @SerializedName("mer_image")
    private String merPhoto;
    @SerializedName("title")
    private String title;
    @SerializedName("discount")
    private String discount;
    @SerializedName("price")
    private String price;
    @SerializedName("from_date")
    private String fromDate;
    @SerializedName("to_date")
    private String toDate;
    @SerializedName("condiction")
    private String condition;

    public Promotion() {
    }

    public Promotion(int id, int merId, String image, String merName, String merPhoto, String title, String discount, String price, String fromDate, String toDate) {
        this.id = id;
        this.merId = merId;
        this.image = image;
        this.merName = merName;
        this.merPhoto = merPhoto;
        this.title = title;
        this.discount = discount;
        this.price = price;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerPhoto() {
        return merPhoto;
    }

    public void setMerPhoto(String merPhoto) {
        this.merPhoto = merPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
