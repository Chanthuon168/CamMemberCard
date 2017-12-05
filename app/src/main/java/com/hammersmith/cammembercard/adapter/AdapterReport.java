package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Report;

import java.util.List;

/**
 * Created by imac on 31/7/17.
 */

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.MyViewHolder> {
    private Activity activity;
    private Context context;
    private List<Report> reports;

    public AdapterReport(Activity activity, List<Report> reports){
        this.activity = activity;
        this.reports = reports;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_report, parent, false);
        AdapterReport.MyViewHolder myViewHolder = new AdapterReport.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
