package com.hammersmith.cammembercard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imac on 28/2/17.
 */
public class ForgotPassword {
    @SerializedName("email")
    private String email;
    @SerializedName("msg")
    private String msg;

    public ForgotPassword() {
    }

    public ForgotPassword(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
