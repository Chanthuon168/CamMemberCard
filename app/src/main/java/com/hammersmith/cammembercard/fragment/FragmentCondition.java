package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterCondition;
import com.hammersmith.cammembercard.model.Condition;
import com.hammersmith.cammembercard.model.Discount;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentCondition extends Fragment {
    private TextView title;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterCondition adapterCondition;
    private List<Condition> conditions = new ArrayList<>();
    private int id;
    private int mdId;
    private String nameMerchandise;
    private String strDiscount;
    private List<Discount> discounts = new ArrayList<>();
    public FragmentCondition() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_condition, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        title = (TextView) view.findViewById(R.id.title);
        DetailActivity activity = (DetailActivity) getActivity();
        id = activity.getMyData();
        nameMerchandise = activity.getNameMerchandise();
        mdId = activity.getMdId();

        ApiInterface serviceDiscount = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Discount>> callDiscount = serviceDiscount.getHighDiscount(mdId);
        callDiscount.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                discounts = response.body();
                strDiscount = discounts.get(0).getDiscount()+"%";
                String first = ""+nameMerchandise+"'s membership card offers up to ";
                String middle = "<font color='#EE0000'>"+strDiscount+"</font>";
                String end = " discount! Collect Now!";
                title.setText(Html.fromHtml(first + middle + end));
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {

            }
        });

        ApiInterface serviceCondition = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Condition>> callCondition = serviceCondition.getCondition(id);
        callCondition.enqueue(new Callback<List<Condition>>() {
            @Override
            public void onResponse(Call<List<Condition>> call, Response<List<Condition>> response) {
                conditions = response.body();
                adapterCondition = new AdapterCondition(getActivity(), conditions);
                recyclerView.setAdapter(adapterCondition);
                adapterCondition.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Condition>> call, Throwable t) {

            }
        });



        return view;
    }
}
