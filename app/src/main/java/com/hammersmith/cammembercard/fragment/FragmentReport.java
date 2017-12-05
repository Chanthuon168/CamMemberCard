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
import com.hammersmith.cammembercard.adapter.AdapterReport;
import com.hammersmith.cammembercard.adapter.AdapterReportAll;
import com.hammersmith.cammembercard.model.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imac on 10/2/17.
 */
public class FragmentReport extends Fragment {

    private LinearLayoutManager layoutManager, layoutManagerAll;
    private AdapterReport adapter;
    private AdapterReportAll adapterAll;
    private List<Report> reports = new ArrayList<>();
    private RecyclerView recyclerViewToday, recyclerViewAllDays;

    public FragmentReport() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        recyclerViewToday = (RecyclerView) view.findViewById(R.id.recyclerViewToday);
        recyclerViewAllDays = (RecyclerView) view.findViewById(R.id.recyclerViewAllDays);
        recyclerViewToday.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new AdapterReport(getActivity(), reports);
        recyclerViewToday.setLayoutManager(layoutManager);
        recyclerViewToday.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        recyclerViewAllDays.setNestedScrollingEnabled(false);
        layoutManagerAll = new LinearLayoutManager(getActivity());
        adapterAll = new AdapterReportAll(getActivity(), reports);
        recyclerViewAllDays.setLayoutManager(layoutManagerAll);
        recyclerViewAllDays.setAdapter(adapterAll);
        adapterAll.notifyDataSetChanged();

        return view;
    }

}
