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
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.model.Merchandise;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchandiseProfile extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RoundedImageView profile;
    private User user, userSocial;
    private Context context;
    private TextView mer_name, mer_email, mer_contact, mer_address, name, email, memId, gender, dateOfBirth, contact, address, country, numUsing, point, numScan, numUserScan, copyright, website;
    private SwipeRefreshLayout swipeRefresh;
    private RatingBar ratingBar;
    private Merchandise merchandise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        numUsing = (TextView) findViewById(R.id.num_using);
        numScan = (TextView) findViewById(R.id.scan);
        point = (TextView) findViewById(R.id.point);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        mer_name = (TextView) findViewById(R.id.name_merchandise);
        mer_email = (TextView) findViewById(R.id.email_merchandise);
        mer_contact = (TextView) findViewById(R.id.contact_merchandise);
        mer_address = (TextView) findViewById(R.id.address_merchandise);
        numUserScan = (TextView) findViewById(R.id.user_scan);
        website = (TextView) findViewById(R.id.website);
        copyright = (TextView) findViewById(R.id.copyright);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);
        findViewById(R.id.lEdit).setOnClickListener(this);
        toolbar.setTitle("Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ApiInterface serviceMerchandise = ApiClient.getClient().create(ApiInterface.class);
        Call<Merchandise> callMerchandise = serviceMerchandise.getMerchandise(user.getSocialLink());
        callMerchandise.enqueue(new Callback<Merchandise>() {
            @Override
            public void onResponse(Call<Merchandise> call, Response<Merchandise> response) {
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);
                merchandise = response.body();
                if (merchandise != null) {
                    name.setText(merchandise.getName());
                    email.setText(merchandise.getEmail());
                    Uri uri = Uri.parse(merchandise.getPhoto());
                    context = profile.getContext();
                    Picasso.with(context).load(uri).into(profile);
                    numUsing.setText("" + merchandise.getNumUsing());
                    numScan.setText("" + merchandise.getNumScan());
                    point.setText("" + merchandise.getPoint());
                    ratingBar.setRating(Float.parseFloat(merchandise.getRating()));
                    memId.setText(merchandise.getMemId());
                    gender.setText(merchandise.getGender());
                    dateOfBirth.setText(merchandise.getDateOfBirth());
                    contact.setText(merchandise.getContact());
                    address.setText(merchandise.getAddress());
                    country.setText(merchandise.getCountry());
                    mer_name.setText(merchandise.getMerName());
                    mer_email.setText(merchandise.getMerEmail());
                    mer_address.setText(merchandise.getMerAddress());
                    numUserScan.setText("" + merchandise.getUserScan());
                    copyright.setText("2017 "+merchandise.getMerName()+" Co.Ltd. All right reserved.");
                    website.setText(merchandise.getWebsite());
                    if (merchandise.getMerContactAlternate().equals("")) {
                        mer_contact.setText(merchandise.getMerContact());
                    } else {
                        mer_contact.setText(merchandise.getMerContact() + "/" + merchandise.getMerContactAlternate());
                    }
                }
            }

            @Override
            public void onFailure(Call<Merchandise> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lEdit:
                startActivity(new Intent(MerchandiseProfile.this, UpdateMerchandiseAccountActivity.class));
                break;
        }
    }
}
