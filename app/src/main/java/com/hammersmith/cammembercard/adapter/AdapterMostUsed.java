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
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.RoundedImageView;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.MostUsed;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by imac on 25/1/17.
 */
public class AdapterMostUsed extends RecyclerView.Adapter<AdapterMostUsed.MyViewHolder> {
    private Activity activity;
    private List<MostUsed> cards;
    private Context context;
    private CollectionCard card;
    private User user;

    public AdapterMostUsed(Activity activity, List<MostUsed> cards) {
        this.activity = activity;
        this.cards = cards;
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
        if (cards.get(position).getSizeStats().equals("valid")) {
            Uri uri = Uri.parse(ApiClient.BASE_URL + cards.get(position).getImgCard());
            context = holder.image.getContext();
            Picasso.with(context).load(uri).into(holder.image);
            Uri uriProfile = Uri.parse(ApiClient.BASE_URL + cards.get(position).getImgMerchandise());
            context = holder.profile.getContext();
            Picasso.with(context).load(uriProfile).into(holder.profile);
            holder.name.setText(cards.get(position).getName());
            holder.address.setText(cards.get(position).getAddress());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("id", cards.get(position).getId());
                    intent.putExtra("md_id", cards.get(position).getMerId());
                    intent.putExtra("exp", cards.get(position).getExpDate());
                    intent.putExtra("name", cards.get(position).getName());
                    intent.putExtra("image_card", cards.get(position).getImgCard());
                    intent.putExtra("logo", cards.get(position).getImgMerchandise());
                    intent.putExtra("status", cards.get(position).getStatus());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.startActivity(intent);
                }
            });

            holder.numUser.setText(cards.get(position).getCount());
            if (cards.get(position).getStatus().equals("checked")) {
                holder.imgCard.setImageResource(R.drawable.new_gift);
            } else {
                holder.imgCard.setImageResource(R.drawable.new_card);
            }

            holder.imgCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card = new CollectionCard(user.getSocialLink(), cards.get(position).getId());
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
                                holder.numUser.setText("");
                            } else {
                                holder.numUser.setText(card.getCount());
                            }
                        }

                        @Override
                        public void onFailure(Call<CollectionCard> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RoundedImageView profile, imgCard;
        TextView name, address, numUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCard = (RoundedImageView) itemView.findViewById(R.id.imgCard);
            image = (ImageView) itemView.findViewById(R.id.image_card);
            profile = (RoundedImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            numUser = (TextView) itemView.findViewById(R.id.numUser);
        }
    }
}
