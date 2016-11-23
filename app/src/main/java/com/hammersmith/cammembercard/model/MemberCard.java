package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 11/22/2016.
 */
public class MemberCard {
    @SerializedName("id")
    private int id;
    @SerializedName("img_card")
    private String ImgCard;
    @SerializedName("img_merchandise")
    private String imgMerchandise;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("exp")
    private String expDate;

    public MemberCard(){}

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
}
