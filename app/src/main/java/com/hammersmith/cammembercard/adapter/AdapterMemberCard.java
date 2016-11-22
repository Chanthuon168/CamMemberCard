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

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.RoundedImageView;
import com.hammersmith.cammembercard.model.MemberCard;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chan Thuon on 11/2/2016.
 */
public class AdapterMemberCard extends RecyclerView.Adapter<AdapterMemberCard.MyViewHolder> {
    private Activity activity;
    private List<MemberCard> members;
    private Context context;

    public AdapterMemberCard(Activity activity, List<MemberCard> members) {
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
        Uri uri = Uri.parse(ApiClient.BASE_URL + members.get(position).getImgCard());
        context = holder.image.getContext();
        Picasso.with(context).load(uri).into(holder.image);
        Uri uriProfile = Uri.parse(ApiClient.BASE_URL + members.get(position).getImgMerchandise());
        context = holder.profile.getContext();
        Picasso.with(context).load(uriProfile).into(holder.profile);
        holder.name.setText(members.get(position).getName());
        holder.address.setText(members.get(position).getAddress());
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
                holder.imgCard.setImageDrawable(activity.getResources().getDrawable(R.drawable.new_gift));
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, profile;
        RoundedImageView imgCard;
        TextView name, address;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCard = (RoundedImageView) itemView.findViewById(R.id.imgCard);
            image = (ImageView) itemView.findViewById(R.id.image_card);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }
}
