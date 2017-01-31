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

import com.hammersmith.cammembercard.adapter.AdapterMemberCard;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberCardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterMemberCard adapterMemberCard;
    private LinearLayoutManager layoutManager;
    private List<MemberCard> members = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private int sizeMembership;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_card);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Membership Card");
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
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
        Call<List<MemberCard>> callMember = serviceMembership.getMembershipCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                members = response.body();
                swipeRefresh.setRefreshing(false);
                Log.d("membercard", members.toArray().toString());
                sizeMembership = members.size();
                adapterMemberCard = new AdapterMemberCard(MemberCardActivity.this, members);
                recyclerView.setAdapter(adapterMemberCard);
                adapterMemberCard.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MemberCard>> call, Throwable t) {

            }
        });

    }
    private void refreshData() {
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MemberCard>> callMember = serviceMembership.getMembershipCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                members = response.body();
                if (sizeMembership != members.size()) {
                    adapterMemberCard = new AdapterMemberCard(MemberCardActivity.this, members);
                    recyclerView.setAdapter(adapterMemberCard);
                    adapterMemberCard.notifyDataSetChanged();
                    sizeMembership = members.size();
                }
                swipeRefresh.setRefreshing(false);
                adapterMemberCard.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MemberCard>> call, Throwable t) {

            }
        });
    }
}
