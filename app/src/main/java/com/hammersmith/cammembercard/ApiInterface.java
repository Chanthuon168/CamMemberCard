package com.hammersmith.cammembercard;

import com.hammersmith.cammembercard.model.Account;
import com.hammersmith.cammembercard.model.Album;
import com.hammersmith.cammembercard.model.Condition;
import com.hammersmith.cammembercard.model.Discount;
import com.hammersmith.cammembercard.model.ForgotPassword;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.Merchandise;
import com.hammersmith.cammembercard.model.Scan;
import com.hammersmith.cammembercard.model.MostUsed;
import com.hammersmith.cammembercard.model.Outlet;
import com.hammersmith.cammembercard.model.Promotion;
import com.hammersmith.cammembercard.model.Review;
import com.hammersmith.cammembercard.model.Scanned;
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

    @GET("get/discount/{id}/{user_link}")
    Call<List<Discount>> getDiscount(@Path("id") int id, @Path("user_link") String userLink);

    @POST("add_card")
    Call<CollectionCard> addCard(@Body CollectionCard card);

    @GET("card_collection/{social_link}")
    Call<List<MemberCard>> getCollectionCard(@Path("social_link") String socialLink);

    @GET("most_used/{social_link}")
    Call<List<MostUsed>> getMostUsedCard(@Path("social_link") String socialLink);

    @GET("term_condition/{card_id}")
    Call<List<Condition>> getCondition(@Path("card_id") int card_id);

    @GET("high_discount/{md_id}")
    Call<List<Discount>> getHighDiscount(@Path("md_id") int md_id);

    @GET("get_merchandise/{mer_link}")
    Call<Merchandise> getMerchandise(@Path("mer_link") String merLink);

    @POST("post_scan")
    Call<Scanned> postScan(@Body Scanned scan);

    @GET("get_scan/{mer_link}")
    Call<List<Scanned>> getUserScan(@Path("mer_link") String merLink);

    @GET("most_scan/{mer_link}")
    Call<List<Scan>> getUserMostScan(@Path("mer_link") String merLink);

    @GET("scan_today/{mer_link}")
    Call<List<Scan>> getUserTodayScan(@Path("mer_link") String merLink);

    @GET("get_scan_detail/{scan_id}")
    Call<List<Scanned>> getScanDetail(@Path("scan_id") int scanId);

    @GET("filter/{user_link}/{name}")
    Call<List<MemberCard>> filterByName(@Path("user_link") String userLink, @Path("name") String name);

    @POST("post_review")
    Call<Review> postReview(@Body Review review);

    @GET("get_review/{mer_id}")
    Call<List<Review>> getReview(@Path("mer_id") int merId);

    @POST("upload_image")
    Call<Account> uploadFile(@Body Account account);

    @POST("update_account")
    Call<Account> updateAccount(@Body Account account);

    @GET("user_history/{user_link}")
    Call<List<Scanned>> getUserScanned(@Path("user_link") String userLink);

    @POST("reset_password")
    Call<Account> resetUserPassword(@Body Account account);

    @POST("forgot_password")
    Call<ForgotPassword> forgotPassword(@Body ForgotPassword ForgotPassword);

    @GET("get_promotion")
    Call<List<Promotion>> getPromotion ();

    @GET("get_membercard_by_promotion/{merId}/{userLink}")
    Call<List<MemberCard>> getMemberCardByPromotion(@Path("merId") int merId,@Path("userLink") String userLink);

    @GET("get_promotion_condition/{proId}")
    Call<List<Promotion>> getProCondition (@Path("proId") int proId);

    @GET("get_outlet/{mer_id}")
    Call<List<Outlet>> getOutlet (@Path("mer_id") int merId);

    @GET("testing")
    Call<User> getTesting (@Body User user);

}
