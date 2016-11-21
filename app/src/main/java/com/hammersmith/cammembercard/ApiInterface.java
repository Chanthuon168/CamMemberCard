package com.hammersmith.cammembercard;

import com.hammersmith.cammembercard.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Chan Thuon on 11/21/2016.
 */
public interface ApiInterface {
    @POST("user/login")
    Call<User> createUserBySocial(@Body User user);

    @POST("get/user")
    Call<User> getUser(@Body User user);

    @POST("user/register")
    Call<User> userRegister(@Body User user);
}
