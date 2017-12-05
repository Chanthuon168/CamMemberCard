package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterUsing;
import com.hammersmith.cammembercard.model.Scan;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by imac on 10/2/17.
 */
public class FragmentMostScanned extends Fragment {
    private RecyclerView recyclerView;
    private AdapterUsing adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private List<Scan> scans = new ArrayList<>();
    private int sizeScan;
    private User user;
    private LinearLayout lMessage;
    private TextView txtMessage;

    public FragmentMostScanned() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_using, container, false);
        user = PrefUtils.getCurrentUser(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        lMessage = (LinearLayout) view.findViewById(R.id.lMessage);
        txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.yellow);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Scan>> scanCall = serviceScan.getUserMostScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<Scan>>() {
            @Override
            public void onResponse(Call<List<Scan>> call, Response<List<Scan>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans.size() > 0) {
                    sizeScan = scans.size();
                    adapter = new AdapterUsing(getActivity(), scans);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no most scan");
                }
            }

            @Override
            public void onFailure(Call<List<Scan>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void refreshData() {
        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Scan>> scanCall = serviceScan.getUserMostScan(user.getSocialLink());
        scanCall.enqueue(new Callback<List<Scan>>() {
            @Override
            public void onResponse(Call<List<Scan>> call, Response<List<Scan>> response) {
                scans = response.body();
                swipeRefresh.setRefreshing(false);
                if (scans != null) {
                    if (sizeScan != scans.size()) {
                        sizeScan = scans.size();
                        adapter = new AdapterUsing(getActivity(), scans);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (sizeScan > 0) {
                            lMessage.setVisibility(View.GONE);
                        }
                    }
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no most scan");
                }
            }

            @Override
            public void onFailure(Call<List<Scan>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
