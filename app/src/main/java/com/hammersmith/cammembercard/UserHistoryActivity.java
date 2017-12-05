package com.hammersmith.cammembercard;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.adapter.AdapterHistory;
import com.hammersmith.cammembercard.model.Scanned;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterHistory adapter;
    private LinearLayoutManager layoutManager;
    private List<Scanned> scans = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private User user;
    private int sizeHistory;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        user = PrefUtils.getCurrentUser(UserHistoryActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.yellow);

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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        Log.d("userling",user.getSocialLink());
        ApiInterface serviceDetail = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Scanned>> callDetail = serviceDetail.getUserScanned(user.getSocialLink());
        callDetail.enqueue(new Callback<List<Scanned>>() {
            @Override
            public void onResponse(Call<List<Scanned>> call, Response<List<Scanned>> response) {
                scans = response.body();
                sizeHistory = scans.size();
                swipeRefresh.setRefreshing(false);
                if (scans.size() > 0){
                    adapter = new AdapterHistory(UserHistoryActivity.this, scans);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Scanned>> call, Throwable t) {

            }
        });
    }

    private void refreshData() {
        ApiInterface serviceDetail = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Scanned>> callDetail = serviceDetail.getUserScanned(user.getSocialLink());
        callDetail.enqueue(new Callback<List<Scanned>>() {
            @Override
            public void onResponse(Call<List<Scanned>> call, Response<List<Scanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans.size() != sizeHistory){
                    sizeHistory = scans.size();
                    if (scans.size() > 0){
                        adapter = new AdapterHistory(UserHistoryActivity.this, scans);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
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
