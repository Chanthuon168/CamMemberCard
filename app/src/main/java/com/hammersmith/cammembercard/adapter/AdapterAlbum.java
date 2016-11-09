package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Album;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}