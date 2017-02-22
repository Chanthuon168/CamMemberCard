package com.hammersmith.cammembercard;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hammersmith.cammembercard.adapter.AdapterTodayScanned;
import com.hammersmith.cammembercard.model.TodayScanned;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTodayScan extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterTodayScanned adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private List<TodayScanned> scans = new ArrayList<>();
    private int sizeScan;
    private User user;
    private LinearLayout lMessage;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_today_scan);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Today Scan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        user = PrefUtils.getCurrentUser(ActivityTodayScan.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        lMessage = (LinearLayout) findViewById(R.id.lMessage);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TodayScanned>> scanCall = serviceScan.getUserTodayScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<TodayScanned>>() {
            @Override
            public void onResponse(Call<List<TodayScanned>> call, Response<List<TodayScanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans.size() > 0) {
                    sizeScan = scans.size();
                    adapter = new AdapterTodayScanned(ActivityTodayScan.this, scans);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no scan today");
                }
            }

            @Override
            public void onFailure(Call<List<TodayScanned>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void refreshData() {
        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TodayScanned>> scanCall = serviceScan.getUserTodayScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<TodayScanned>>() {
            @Override
            public void onResponse(Call<List<TodayScanned>> call, Response<List<TodayScanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans != null) {
                    if (sizeScan != scans.size()) {
                        sizeScan = scans.size();
                        adapter = new AdapterTodayScanned(ActivityTodayScan.this, scans);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no scan today");
                }
            }

            @Override
            public void onFailure(Call<List<TodayScanned>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
