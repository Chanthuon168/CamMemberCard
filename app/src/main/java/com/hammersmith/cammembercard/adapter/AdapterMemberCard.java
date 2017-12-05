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
import android.widget.RatingBar;
import android.widget.TextView;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.RoundedImageView;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 11/2/2016.
 */
public class AdapterMemberCard extends RecyclerView.Adapter<AdapterMemberCard.MyViewHolder> {
    private Activity activity;
    private List<MemberCard> members;
    private Context context;
    private CollectionCard card;
    private User user;
    private MemberCard memberCard;

    public AdapterMemberCard(Activity activity, List<MemberCard> members) {
        this.activity = activity;
        this.members = members;
        user = PrefUtils.getCurrentUser(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Uri uri = Uri.parse(ApiClient.BASE_URL + members.get(position).getImgCard());
        context = holder.image.getContext();
        Picasso.with(context).load(uri).into(holder.image);
        holder.name.setText(members.get(position).getName());
        holder.ratingBar.setRating(Float.parseFloat(members.get(position).getRating()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberCard = members.get(position);
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("member", memberCard);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.numOutlet.setText(members.get(position).getOutlet());
        if (members.get(position).getCount().equals("")) {
            holder.numUser.setText("No used");
        } else {
            holder.numUser.setText(members.get(position).getCount() + " used");
        }

        if (members.get(position).getStatus().equals("checked")) {
            holder.imgCard.setImageResource(R.drawable.new_gift);
        } else {
            holder.imgCard.setImageResource(R.drawable.new_card);
        }

        holder.imgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card = new CollectionCard(user.getSocialLink(), members.get(position).getId());
                ApiInterface serviceAddCard = ApiClient.getClient().create(ApiInterface.class);
                Call<CollectionCard> callCreate = serviceAddCard.addCard(card);
                callCreate.enqueue(new Callback<CollectionCard>() {
                    @Override
                    public void onResponse(Call<CollectionCard> call, Response<CollectionCard> response) {
                        card = response.body();
                        if (card.getStatus().equals("checked")) {
                            holder.imgCard.setImageResource(R.drawable.new_gift);
                        }
                        if (card.getCount().equals("0")) {
                            holder.numUser.setText("No used");
                        } else {
                            holder.numUser.setText(card.getCount() + " used");
                        }
                    }

                    @Override
                    public void onFailure(Call<CollectionCard> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RoundedImageView imgCard;
        TextView name, numUser, numOutlet;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCard = (RoundedImageView) itemView.findViewById(R.id.imgCard);
            image = (ImageView) itemView.findViewById(R.id.image_card);
            name = (TextView) itemView.findViewById(R.id.name);
            numUser = (TextView) itemView.findViewById(R.id.numUser);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            numOutlet = (TextView) itemView.findViewById(R.id.numOutlet);
        }
    }
}
