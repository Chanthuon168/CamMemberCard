package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.hammersmith.cammembercard.ActivityPromotionDetail;
import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Promotion;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by imac on 28/3/17.
 */
public class AdapterPromotion extends RecyclerView.Adapter<AdapterPromotion.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Promotion> promotions;
    private Promotion mPro;

    public AdapterPromotion(Activity activity, List<Promotion> promotions) {
        this.activity = activity;
        this.promotions = promotions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_promotion, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Uri uri = Uri.parse(ApiClient.BASE_URL + promotions.get(position).getImage());
        context = holder.imageView.getContext();
        Picasso.with(context).load(uri).into(holder.imageView);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(promotions.get(position).getToDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat todayFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String dateExpire = todayFormat.format(date);

        holder.title.setText(promotions.get(position).getTitle());
        holder.expire.setText("Expire " + dateExpire);
        holder.discount.setText(promotions.get(position).getDiscount() + "% Off");

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPro = promotions.get(position);
                Intent intent = new Intent(activity, ActivityPromotionDetail.class);
                intent.putExtra("promotion", mPro);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, expire, discount;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            expire = (TextView) itemView.findViewById(R.id.expire);
            discount = (TextView) itemView.findViewById(R.id.discount);
        }
    }
}
