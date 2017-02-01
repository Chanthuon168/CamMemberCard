package com.hammersmith.cammembercard;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hammersmith.cammembercard.adapter.AdapterMostUsed;
import com.hammersmith.cammembercard.model.MostUsed;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostUsedActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterMostUsed adapterMostUsed;
    private LinearLayoutManager layoutManager;
    private List<MostUsed> cards = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ProgressDialog mProgressDialog;
    private int sizeMembership;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_used);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Most Used");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);
        user = PrefUtils.getCurrentUser(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MostUsed>> callMember = serviceMembership.getMostUsedCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MostUsed>>() {
            @Override
            public void onResponse(Call<List<MostUsed>> call, Response<List<MostUsed>> response) {
                cards = response.body();
                Log.d("collection", cards.toArray().toString());
                swipeRefresh.setRefreshing(false);
                if (cards.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    sizeMembership = cards.size();
                    adapterMostUsed = new AdapterMostUsed(MostUsedActivity.this, cards);
                    recyclerView.setAdapter(adapterMostUsed);
                    adapterMostUsed.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MostUsed>> call, Throwable t) {

            }
        });
    }

    private void refreshData() {
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MostUsed>> callMember = serviceMembership.getMostUsedCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MostUsed>>() {
            @Override
            public void onResponse(Call<List<MostUsed>> call, Response<List<MostUsed>> response) {
                cards = response.body();
                Log.d("collection", cards.toArray().toString());
                swipeRefresh.setRefreshing(false);
                if (cards.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    if (sizeMembership != cards.size()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapterMostUsed = new AdapterMostUsed(MostUsedActivity.this, cards);
                        recyclerView.setAdapter(adapterMostUsed);
                        adapterMostUsed.notifyDataSetChanged();
                        sizeMembership = cards.size();
                        adapterMostUsed.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MostUsed>> call, Throwable t) {

            }
        });
    }
}
