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
import com.hammersmith.cammembercard.model.Scanned;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by imac on 10/2/17.
 */
public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Scanned> scans;
    private static String today;


    public AdapterPayment(Activity activity, List<Scanned> scans) {
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
        Uri uri = Uri.parse("" + scans.get(position).getPhoto());
        context = holder.profile.getContext();
        Picasso.with(context).load(uri).into(holder.profile);
        holder.name.setText(scans.get(position).getName());
        holder.discount.setText(scans.get(position).getLastDiscount() + "% Off");
        holder.using_date.setText(getTimeStamp(scans.get(position).getCreateAt()));
        holder.ratingBar.setRating(Float.parseFloat(scans.get(position).getRating()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserDetailActivity.class);
                intent.putExtra("id",scans.get(position).getId());
                intent.putExtra("photo",scans.get(position).getPhoto());
                intent.putExtra("name",scans.get(position).getName());
                intent.putExtra("scanned",scans.get(position).getNumberScanned());
                intent.putExtra("rating",scans.get(position).getRating());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView profile;
        TextView name, discount, using_date;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            profile = (RoundedImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            discount = (TextView) itemView.findViewById(R.id.discount);
            using_date = (TextView) itemView.findViewById(R.id.using_date);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
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
