package com.hammersmith.cammembercard.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterAlbum;
import com.hammersmith.cammembercard.adapter.AdapterOutlet;
import com.hammersmith.cammembercard.model.Album;
import com.hammersmith.cammembercard.model.Outlet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentOutlet extends Fragment {
    private AdapterOutlet adapter;
    private RecyclerView recyclerView;
    private List<Outlet> outlets = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private int id;
    private LinearLayout lProgress;

    public FragmentOutlet() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlet, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        lProgress = (LinearLayout) view.findViewById(R.id.lProgress);
        DetailActivity activity = (DetailActivity) getActivity();
        id = activity.getMyData();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Outlet>> call = service.getOutlet(id);
        call.enqueue(new Callback<List<Outlet>>() {
            @Override
            public void onResponse(Call<List<Outlet>> call, Response<List<Outlet>> response) {
                outlets = response.body();
                adapter = new AdapterOutlet(getActivity(), outlets);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Outlet>> call, Throwable t) {

            }
        });

        return view;
    }
}
