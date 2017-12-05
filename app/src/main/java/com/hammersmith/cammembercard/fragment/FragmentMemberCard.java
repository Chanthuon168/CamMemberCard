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
import com.hammersmith.cammembercard.adapter.AdapterMemberCard;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/2/2016.
 */
public class FragmentMemberCard extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMemberCard adapterMemberCard;
    private LinearLayoutManager layoutManager;
    private List<MemberCard> members = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ProgressDialog mProgressDialog;
    private int sizeMembership;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_card, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        showProgressDialog();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
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
        Call<List<MemberCard>> callMember = serviceMembership.getMembershipCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                members = response.body();
                swipeRefresh.setRefreshing(false);
                Log.d("membercard", members.toArray().toString());
                sizeMembership = members.size();
                adapterMemberCard = new AdapterMemberCard(getActivity(), members);
                recyclerView.setAdapter(adapterMemberCard);
                adapterMemberCard.notifyDataSetChanged();
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
        Call<List<MemberCard>> callMember = serviceMembership.getMembershipCard(user.getSocialLink());
        callMember.enqueue(new Callback<List<MemberCard>>() {
            @Override
            public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                members = response.body();
                hideProgressDialog();
                if (sizeMembership != members.size()) {
                    adapterMemberCard = new AdapterMemberCard(getActivity(), members);
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
