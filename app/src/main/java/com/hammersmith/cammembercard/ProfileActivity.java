package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RoundedImageView profile;
    private User user, userSocial;
    private Context context;
    private TextView name, email, memId, gender, dateOfBirth, contact, address, country, numCard, point, numScan;
    private SwipeRefreshLayout swipeRefresh;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = PrefUtils.getCurrentUser(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile = (RoundedImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        memId = (TextView) findViewById(R.id.memId);
        gender = (TextView) findViewById(R.id.gender);
        dateOfBirth = (TextView) findViewById(R.id.dob);
        contact = (TextView) findViewById(R.id.contact);
        address = (TextView) findViewById(R.id.address);
        country = (TextView) findViewById(R.id.country);
        numCard = (TextView) findViewById(R.id.num_card);
        numScan = (TextView) findViewById(R.id.scan);
        point = (TextView) findViewById(R.id.point);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);
        findViewById(R.id.lEdit).setOnClickListener(this);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userSocial = new User(user.getSocialLink());
        ApiInterface serviceUser = ApiClient.getClient().create(ApiInterface.class);
        Call<User> callUser = serviceUser.getUser(userSocial);
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);
                user = response.body();
                name.setText(user.getName());
                email.setText(user.getEmail());
                Uri uri = Uri.parse(user.getPhoto());
                context = profile.getContext();
                Picasso.with(context).load(uri).into(profile);
                numCard.setText("" + user.getNumCard());
                numScan.setText("" + user.getNumScan());
                point.setText("" + user.getPoint());
                ratingBar.setRating(Float.parseFloat(user.getRating()));
                memId.setText(user.getMemId());
                gender.setText(user.getGender());
                dateOfBirth.setText(user.getDateOfBirth());
                contact.setText(user.getContact());
                address.setText(user.getAddress());
                country.setText(user.getCountry());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lEdit:
                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
                break;
        }
    }
}
