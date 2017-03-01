package com.hammersmith.cammembercard.model;

import com.google.firebase.auth.api.model.StringList;
import com.google.gson.annotations.SerializedName;

/**
 * Created by imac on 23/1/17.
 */
public class CollectionCard {
    @SerializedName("id")
    private int id;
    @SerializedName("card_id")
    private int cardId;
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

    public CollectionCard() {
    }

    public CollectionCard(String socialLink, int cardId) {
        this.socialLink = socialLink;
        this.cardId = cardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public int getMerId() {
        return merId;
    }

    public void setMerId(int merId) {
        this.merId = merId;
    }

    public String getImgCard() {
        return ImgCard;
    }

    public void setImgCard(String imgCard) {
        ImgCard = imgCard;
    }

    public String getImgMerchandise() {
        return imgMerchandise;
    }

    public void setImgMerchandise(String imgMerchandise) {
        this.imgMerchandise = imgMerchandise;
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

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
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
}
