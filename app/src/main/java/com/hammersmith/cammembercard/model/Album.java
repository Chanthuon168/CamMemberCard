package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class Album {
    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String image;
    @SerializedName("mem_id")
    private int memId;

    public Album() {
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
