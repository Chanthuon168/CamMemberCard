package com.hammersmith.cammembercard;

import com.hammersmith.cammembercard.model.Album;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @POST("user/loginaccount")
    Call<User> userLoginByEmail(@Body User user);

    @GET("get/membercard")
    Call<List<MemberCard>> getMembershipCard();

    @GET("get/album/{id}")
    Call<List<Album>> getAlbum(@Path("id") int id);
}
