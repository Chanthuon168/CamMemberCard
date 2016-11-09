package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Member;

import java.util.List;

/**
 * Created by Chan Thuon on 11/2/2016.
 */
public class AdapterMemberCard extends RecyclerView.Adapter<AdapterMemberCard.MyViewHolder> {
    private Activity activity;
    private List<Member> members;

    public AdapterMemberCard(Activity activity, List<Member> members) {
        this.activity = activity;
        this.members = members;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            }
        });
        holder.imgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgCard.setImageDrawable(activity.getResources().getDrawable(R.drawable.img_gift));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, imgCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCard = (ImageView) itemView.findViewById(R.id.imgCard);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
