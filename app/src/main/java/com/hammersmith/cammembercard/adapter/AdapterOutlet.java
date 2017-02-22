package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Outlet;

import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class AdapterOutlet extends RecyclerView.Adapter<AdapterOutlet.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Outlet> outlets;

    public AdapterOutlet(Activity activity, List<Outlet> outlets) {
        this.activity = activity;
        this.outlets = outlets;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_outlet, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOutlet();
            }
        });
    }

    private void dialogOutlet() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View viewDialog = factory.inflate(R.layout.dialog_outlet, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
