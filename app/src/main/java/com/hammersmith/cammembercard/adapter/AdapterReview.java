package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Review;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class AdapterReview extends RecyclerView.Adapter<AdapterReview.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Review> reviews;
    private static String today;

    public AdapterReview(Activity activity, List<Review> reviews) {
        this.activity = activity;
        this.reviews = reviews;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_review, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Uri uri = Uri.parse(reviews.get(position).getProfile());
        context = holder.profile.getContext();
        Picasso.with(context).load(uri).into(holder.profile);
        holder.name.setText(reviews.get(position).getName());
        holder.ratingBar.setRating(Float.parseFloat(reviews.get(position).getRating()));
        holder.comment.setText(reviews.get(position).getComment());
        holder.createdAt.setText(getTimeStamp(reviews.get(position).getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, comment, createdAt;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            createdAt = (TextView) itemView.findViewById(R.id.createdAt);
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
