package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imac on 13/2/17.
 */
public class Scanned {
    @SerializedName("id")
    private int id;
    @SerializedName("social_link")
    private String socialLink;
    @SerializedName("scanner_link")
    private String scannerLink;
    @SerializedName("mer_id")
    private int merId;
    @SerializedName("discount")
    private String discount;
    @SerializedName("smg")
    private String smg;
    @SerializedName("photo")
    private String photo;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("last_discount")
    private String lastDiscount;
    @SerializedName("last_paid")
    private String lastPaid;
    @SerializedName("last_save")
    private String lastSave;
    @SerializedName("created_at")
    private String createAt;
    @SerializedName("scanned")
    private String numberScanned;
    @SerializedName("rating")
    private String rating;
    @SerializedName("amount")
    private String amount;
    @SerializedName("status")
    private String status;
    @SerializedName("paid")
    private String paid;
    @SerializedName("save")
    private String save;
    @SerializedName("sub_total")
    private String subTotal;
    @SerializedName("grand_total")
    private String grandTotal;

    public Scanned() {
    }

    public Scanned(String amount, String socialLink, String scannerLink, int merId, String discount, String createdAt) {
        this.amount = amount;
        this.socialLink = socialLink;
        this.scannerLink = scannerLink;
        this.merId = merId;
        this.discount = discount;
        this.createAt = createdAt;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getLastPaid() {
        return lastPaid;
    }

    public void setLastPaid(String lastPaid) {
        this.lastPaid = lastPaid;
    }

    public String getLastSave() {
        return lastSave;
    }

    public void setLastSave(String lastSave) {
        this.lastSave = lastSave;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Scanned(int merId) {
        this.merId = merId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSmg() {
        return smg;
    }

    public void setSmg(String smg) {
        this.smg = smg;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastDiscount() {
        return lastDiscount;
    }

    public void setLastDiscount(String lastDiscount) {
        this.lastDiscount = lastDiscount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getNumberScanned() {
        return numberScanned;
    }

    public void setNumberScanned(String numberScanned) {
        this.numberScanned = numberScanned;
    }

    public String getScannerLink() {
        return scannerLink;
    }

    public void setScannerLink(String scannerLink) {
        this.scannerLink = scannerLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
