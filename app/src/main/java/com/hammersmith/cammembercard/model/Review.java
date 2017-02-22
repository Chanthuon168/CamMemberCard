package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class Review {
    @SerializedName("id")
    private int id;
    @SerializedName("mer_id")
    private int merId;
    @SerializedName("user_link")
    private String userLink;
    @SerializedName("comment")
    private String comment;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("msg")
    private String msg;
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String profile;

    public Review() {
    }

    public Review(int merId, String userLink, String comment, String createdAt) {
        this.merId = merId;
        this.userLink = userLink;
        this.comment = comment;
        this.createdAt = createdAt;
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

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
