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
import android.widget.LinearLayout;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterCollection;
import com.hammersmith.cammembercard.adapter.AdapterMemberCard;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/23/2016.
 */
public class FragmentCollection extends Fragment {
    private RecyclerView recyclerView;
    private AdapterCollection adapterCollection;
    private LinearLayoutManager layoutManager;
    private List<MemberCard> collections = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ProgressDialog mProgressDialog;
    private int sizeMembership;
    private User user;
    private LinearLayout lNoFavorite;

    public FragmentCollection() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        showProgressDialog();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        lNoFavorite = (LinearLayout) view.findViewById(R.id.lNoFavorite);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.yellow);

        user = PrefUtils.getCurrentUser(getActivity());
        Log.d("userId", user.getSocialLink());

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MemberCard>> callMember = serviceMembership.getCollectionCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                collections = response.body();
                Log.d("collection", collections.toArray().toString());
                swipeRefresh.setRefreshing(false);
                if (collections.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                    lNoFavorite.setVisibility(View.VISIBLE);
                } else {
                    lNoFavorite.setVisibility(View.GONE);
                    sizeMembership = collections.size();
                    adapterCollection = new AdapterCollection(getActivity(), collections);
                    recyclerView.setAdapter(adapterCollection);
                    adapterCollection.notifyDataSetChanged();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<List<MemberCard>> call, Throwable t) {

            }
        });


        return view;
    }

    private void refreshData() {
        ApiInterface serviceMembership = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MemberCard>> callMember = serviceMembership.getCollectionCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                collections = response.body();
                Log.d("collection", collections.toArray().toString());
                hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                if (collections.get(0).getSizeStats().equals("invalid")) {
                    recyclerView.setVisibility(View.GONE);
                    lNoFavorite.setVisibility(View.VISIBLE);
                } else {
                    lNoFavorite.setVisibility(View.GONE);
                    if (sizeMembership != collections.size()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapterCollection = new AdapterCollection(getActivity(), collections);
                        recyclerView.setAdapter(adapterCollection);
                        adapterCollection.notifyDataSetChanged();
                        sizeMembership = collections.size();
                        adapterCollection.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MemberCard>> call, Throwable t) {

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
