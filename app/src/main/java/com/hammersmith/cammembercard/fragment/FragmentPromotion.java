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
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterPromotion;
import com.hammersmith.cammembercard.model.Promotion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by imac on 28/3/17.
 */
public class FragmentPromotion extends Fragment {
    private RecyclerView recyclerView;
    private AdapterPromotion adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private List<Promotion> promotions = new ArrayList<>();
    private int sizePromotion;
    private LinearLayout lMessage;
    private TextView txtMessage;


    public FragmentPromotion() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
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


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Promotion>> scanCall = service.getPromotion();
        scanCall.enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                promotions = response.body();
                swipeRefresh.setRefreshing(false);
                if (promotions.size() > 0) {
                    sizePromotion = promotions.size();
                    adapter = new AdapterPromotion(getActivity(), promotions);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no promotion");
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void refreshData() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Promotion>> scanCall = service.getPromotion();
        scanCall.enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                promotions = response.body();
                swipeRefresh.setRefreshing(false);
                if (promotions != null) {
                    if (sizePromotion != promotions.size()) {
                        sizePromotion = promotions.size();
                        adapter = new AdapterPromotion(getActivity(), promotions);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    lMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("There are no promotion");
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
