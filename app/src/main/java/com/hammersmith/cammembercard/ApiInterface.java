package com.hammersmith.cammembercard;

import com.hammersmith.cammembercard.model.Album;
import com.hammersmith.cammembercard.model.Condition;
import com.hammersmith.cammembercard.model.Discount;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.MostUsed;
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

    @GET("get/membercard/{social_link}")
    Call<List<MemberCard>> getMembershipCard(@Path("social_link") String socialLink);

    @GET("get/album/{id}")
    Call<List<Album>> getAlbum(@Path("id") int id);

    @GET("get/discount/{id}")
    Call<List<Discount>> getDiscount(@Path("id") int id);

    @POST("add_card")
    Call<CollectionCard> addCard(@Body CollectionCard card);

    @GET("card_collection/{social_link}")
    Call<List<CollectionCard>> getCollectionCard(@Path("social_link") String socialLink);

    @GET("most_used/{social_link}")
    Call<List<MostUsed>> getMostUsedCard(@Path("social_link") String socialLink);

    @GET("term_condition/{card_id}")
    Call<List<Condition>> getCondition(@Path("card_id") int card_id);

    @GET("high_discount/{md_id}")
    Call<List<Discount>> getHighDiscount(@Path("md_id") int md_id);
}
