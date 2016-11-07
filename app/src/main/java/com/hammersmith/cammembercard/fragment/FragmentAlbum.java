package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterAlbum;
import com.hammersmith.cammembercard.model.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentAlbum extends Fragment {
    private AdapterAlbum adapterAlbum;
    private RecyclerView recyclerView;
    private List<Album> albums = new ArrayList<>();
    private GridLayoutManager layoutManager;

    public FragmentAlbum() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        adapterAlbum = new AdapterAlbum(getActivity(), albums);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAlbum);
        adapterAlbum.notifyDataSetChanged();
        return view;
    }
}
