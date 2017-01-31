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
import android.widget.LinearLayout;

import com.hammersmith.cammembercard.adapter.AdapterCollection;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterCollection adapterCollection;
    private LinearLayoutManager layoutManager;
    private List<CollectionCard> collections = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ProgressDialog mProgressDialog;
    private int sizeMembership;
    private User user;
    private LinearLayout lNoFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Collection");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        lNoFavorite = (LinearLayout)findViewById(R.id.lNoFavorite);
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
        Call<List<CollectionCard>> callMember = serviceMembership.getCollectionCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<CollectionCard>>() {
            @Override
            public void onResponse(Call<List<CollectionCard>> call, Response<List<CollectionCard>> response) {
                collections = response.body();
                Log.d("collection", collections.toArray().toString());
                swipeRefresh.setRefreshing(false);
                if (collections.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                    lNoFavorite.setVisibility(View.VISIBLE);
                } else {
                    lNoFavorite.setVisibility(View.GONE);
                    sizeMembership = collections.size();
                    adapterCollection = new AdapterCollection(CollectionActivity.this, collections);
                    recyclerView.setAdapter(adapterCollection);
                    adapterCollection.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CollectionCard>> call, Throwable t) {

            }
        });

    }
    private void refreshData() {
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CollectionCard>> callMember = serviceMembership.getCollectionCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<CollectionCard>>() {
            @Override
            public void onResponse(Call<List<CollectionCard>> call, Response<List<CollectionCard>> response) {
                collections = response.body();
                Log.d("collection", collections.toArray().toString());
                swipeRefresh.setRefreshing(false);
                if (collections.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                    lNoFavorite.setVisibility(View.VISIBLE);
                } else {
                    lNoFavorite.setVisibility(View.GONE);
                    if (sizeMembership != collections.size()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapterCollection = new AdapterCollection(CollectionActivity.this, collections);
                        recyclerView.setAdapter(adapterCollection);
                        adapterCollection.notifyDataSetChanged();
                        sizeMembership = collections.size();
                        adapterCollection.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CollectionCard>> call, Throwable t) {

            }
        });
    }
}
