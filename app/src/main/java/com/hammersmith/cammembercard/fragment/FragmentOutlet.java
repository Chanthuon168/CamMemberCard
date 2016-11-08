package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterOutlet;
import com.hammersmith.cammembercard.model.Outlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentOutlet extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterOutlet adapterOutlet;
    private List<Outlet> outlets = new ArrayList<>();

    public FragmentOutlet() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlet, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adapterOutlet = new AdapterOutlet(getActivity(), outlets);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterOutlet);
        return view;
    }
}
