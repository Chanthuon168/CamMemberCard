package com.hammersmith.cammembercard.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.hammersmith.cammembercard.model.Album;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentAlbum extends Fragment {
    private AdapterAlbum adapterAlbum;
    private RecyclerView recyclerView;
    private List<Album> albums = new ArrayList<>();
    private GridLayoutManager layoutManager;
    private int id;
    private ProgressDialog mProgressDialog;
    private LinearLayout lProgress;

    public FragmentAlbum() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lProgress = (LinearLayout) view.findViewById(R.id.lProgress);
        DetailActivity activity = (DetailActivity) getActivity();
        id = activity.getMyData();
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
//        showProgressDialog();
        ApiInterface serviceAlbum = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Album>> callAlbum = serviceAlbum.getAlbum(id);
        callAlbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albums = response.body();
                adapterAlbum = new AdapterAlbum(getActivity(), albums);
                recyclerView.setAdapter(adapterAlbum);
                adapterAlbum.notifyDataSetChanged();
                hideProgressDialog();
                lProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

        return view;
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
