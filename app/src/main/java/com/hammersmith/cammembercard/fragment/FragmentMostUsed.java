package com.hammersmith.cammembercard.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterCollection;
import com.hammersmith.cammembercard.adapter.AdapterMostUsed;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.MostUsed;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/23/2016.
 */
public class FragmentMostUsed extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMostUsed adapterMostUsed;
    private LinearLayoutManager layoutManager;
    private List<MostUsed> cards = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ProgressDialog mProgressDialog;
    private int sizeMembership;
    private User user;
    public FragmentMostUsed(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_most_used, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        showProgressDialog();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);

        user = PrefUtils.getCurrentUser(getActivity());
        Log.d("userId", user.getSocialLink());

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
                    adapterMostUsed = new AdapterMostUsed(getActivity(), cards);
                    recyclerView.setAdapter(adapterMostUsed);
                    adapterMostUsed.notifyDataSetChanged();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<List<MostUsed>> call, Throwable t) {

            }
        });


        return view;
    }

    private void refreshData() {
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MostUsed>> callMember = serviceMembership.getMostUsedCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MostUsed>>() {
            @Override
            public void onResponse(Call<List<MostUsed>> call, Response<List<MostUsed>> response) {
                cards = response.body();
                Log.d("collection", cards.toArray().toString());
                hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                if (cards.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    if (sizeMembership != cards.size()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapterMostUsed = new AdapterMostUsed(getActivity(), cards);
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}
