package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.UserDetailActivity;
import com.hammersmith.cammembercard.model.Scanned;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by imac on 10/2/17.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Scanned> scans;
    private static String today;

    public AdapterHistory(Activity activity, List<Scanned> scans) {
        this.activity = activity;
        this.scans = scans;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.subTotal.setText("$" + scans.get(position).getSubTotal());
        holder.discount.setText(scans.get(position).getDiscount() + "% Off");
        holder.save.setText("$"+scans.get(position).getSave());
        holder.grandTotal.setText("$" + scans.get(position).getGrandTotal());
        Uri uri = Uri.parse(scans.get(position).getPhoto());
        context = holder.photo.getContext();
        Picasso.with(context).load(uri).into(holder.photo);
        holder.name.setText(scans.get(position).getName());
        holder.email.setText(scans.get(position).getEmail());
        holder.createdAt.setText(getTimeStamp(scans.get(position).getCreateAt()));
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView discount, name, email, subTotal, grandTotal, save, createdAt;
        ImageView photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            discount = (TextView) itemView.findViewById(R.id.discount);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
            subTotal = (TextView) itemView.findViewById(R.id.sub_total);
            save = (TextView) itemView.findViewById(R.id.save);
            grandTotal = (TextView) itemView.findViewById(R.id.grand_total);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
