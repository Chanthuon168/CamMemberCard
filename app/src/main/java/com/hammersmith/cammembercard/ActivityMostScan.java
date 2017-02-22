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

import com.hammersmith.cammembercard.adapter.AdapterMostScanned;
import com.hammersmith.cammembercard.model.MostScanned;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMostScan extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterMostScanned adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private List<MostScanned> scans = new ArrayList<>();
    private int sizeScan;
    private User user;
    private LinearLayout lMessage;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_most_scan);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Most Scan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        user = PrefUtils.getCurrentUser(ActivityMostScan.this);
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
        Call<List<MostScanned>> scanCall = serviceScan.getUserMostScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<MostScanned>>() {
            @Override
            public void onResponse(Call<List<MostScanned>> call, Response<List<MostScanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans .size() > 0) {
                    sizeScan = scans.size();
                    adapter = new AdapterMostScanned(ActivityMostScan.this, scans);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no most scan");
                }
            }

            @Override
            public void onFailure(Call<List<MostScanned>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshData() {
        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MostScanned>> scanCall = serviceScan.getUserMostScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<MostScanned>>() {
            @Override
            public void onResponse(Call<List<MostScanned>> call, Response<List<MostScanned>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans != null) {
                    if (sizeScan != scans.size()) {
                        sizeScan = scans.size();
                        adapter = new AdapterMostScanned(ActivityMostScan.this, scans);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no most scan");
                }
            }

            @Override
            public void onFailure(Call<List<MostScanned>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
