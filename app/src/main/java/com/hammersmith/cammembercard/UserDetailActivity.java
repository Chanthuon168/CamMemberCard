package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.adapter.AdapterHistory;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.Scan;
import com.hammersmith.cammembercard.model.Scanned;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterHistory adapter;
    private LinearLayoutManager layoutManager;
    private List<Scanned> scans = new ArrayList<>();
    private String strProfile, strName, strScanned, strRating;
    private TextView name, scanned;
    private RoundedImageView profile;
    private Context context;
    private int id;
    private SwipeRefreshLayout swipeRefresh;
    private RatingBar ratingBar;
    private Scan mScan;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        profile = (RoundedImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        scanned = (TextView) findViewById(R.id.scanned);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);

        recyclerView.setNestedScrollingEnabled(false);
        toolbar.setTitle("Scanned Histories");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });

        Intent intent = getIntent();
        mScan = (Scan) intent.getSerializableExtra("scan");
        if (mScan != null) {
            id = mScan.getId();
            strProfile = mScan.getPhoto();
            strName = mScan.getName();
            strScanned = mScan.getNumberScanned();
            strRating = mScan.getRating();
            Uri uri = Uri.parse(strProfile);
            context = profile.getContext();
            Picasso.with(context).load(uri).into(profile);
            name.setText(strName);
            ratingBar.setRating(Float.parseFloat(strRating));
            int num_scanned = Integer.parseInt(strScanned);
            String time = "";
            if (num_scanned > 1) {
                time = " times";
            } else {
                time = " time";
            }
            scanned.setText("Scanned " + num_scanned + time);
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);


        ApiInterface serviceDetail = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Scanned>> callDetail = serviceDetail.getScanDetail(id);
        callDetail.enqueue(new Callback<List<Scanned>>() {
            @Override
            public void onResponse(Call<List<Scanned>> call, Response<List<Scanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);
                if (scans.size() > 0) {
                    adapter = new AdapterHistory(UserDetailActivity.this, scans);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Scanned>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
