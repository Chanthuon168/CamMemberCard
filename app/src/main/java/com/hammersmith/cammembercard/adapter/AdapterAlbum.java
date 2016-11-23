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

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ImageActivity;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Album> albums;

    public AdapterAlbum(Activity activity, List<Album> albums) {
        this.activity = activity;
        this.albums = albums;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_album, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Uri uri = Uri.parse(ApiClient.BASE_URL + albums.get(position).getImage());
        context = holder.image.getContext();
        Picasso.with(context).load(uri).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtra("id", albums.get(position).getMemId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
