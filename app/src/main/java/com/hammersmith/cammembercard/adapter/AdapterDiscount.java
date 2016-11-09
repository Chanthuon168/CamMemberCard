package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Discount;

import java.util.List;

/**
 * Created by Chan Thuon on 11/9/2016.
 */
public class AdapterDiscount extends RecyclerView.Adapter<AdapterDiscount.MyViewHolder> {
    private Activity activity;
    private List<Discount> discounts;

    public AdapterDiscount(Activity activity, List<Discount> discounts) {
        this.activity = activity;
        this.discounts = discounts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_discount, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 0) {
            holder.layout.setClickable(true);
            holder.number.setText("10");
            holder.number.setTextColor(activity.getResources().getColor(R.color.red));
            holder.symbol.setTextColor(activity.getResources().getColor(R.color.red));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogScan();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, symbol;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.number);
            symbol = (TextView) itemView.findViewById(R.id.symbol);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    private void dialogScan() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View viewDialog = factory.inflate(R.layout.dialog_scan, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView imgCode = (ImageView) viewDialog.findViewById(R.id.imgCode);

        dialog.show();
    }
}
