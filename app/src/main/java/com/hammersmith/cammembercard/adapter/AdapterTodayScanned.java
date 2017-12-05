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
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.RoundedImageView;
import com.hammersmith.cammembercard.UserDetailActivity;
import com.hammersmith.cammembercard.model.Scan;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by imac on 10/2/17.
 */
public class AdapterTodayScanned extends RecyclerView.Adapter<AdapterTodayScanned.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Scan> scans;
    private static String today;
    private Scan scan;

    public AdapterTodayScanned(Activity activity, List<Scan> scans) {
        this.activity = activity;
        this.scans = scans;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_using, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        scan = scans.get(position);
        Uri uri = Uri.parse("" + scan.getPhoto());
        context = holder.profile.getContext();
        Picasso.with(context).load(uri).into(holder.profile);
        holder.name.setText(scan.getName());
        holder.discount.setText("Discount: " + scan.getLastDiscount() + "% Off");
        holder.subTotal.setText("Sub Total: $" + scan.getLastPaid());
        holder.save.setText("Save: $" + scan.getLastSave());
        holder.grandTotal.setText("Grand Total: $"+ scan.getGrandTotal());
        holder.using_date.setText(getTimeStamp(scan.getCreateAt()));
        holder.ratingBar.setRating(Float.parseFloat(scan.getRating()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan = scans.get(position);
                Intent intent = new Intent(activity, UserDetailActivity.class);
                intent.putExtra("scan", scan);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView profile;
        TextView name, discount, using_date, subTotal, grandTotal, save;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            profile = (RoundedImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            discount = (TextView) itemView.findViewById(R.id.discount);
            using_date = (TextView) itemView.findViewById(R.id.using_date);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            subTotal = (TextView) itemView.findViewById(R.id.subTotal);
            save = (TextView) itemView.findViewById(R.id.save);
            grandTotal = (TextView) itemView.findViewById(R.id.paid);

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
