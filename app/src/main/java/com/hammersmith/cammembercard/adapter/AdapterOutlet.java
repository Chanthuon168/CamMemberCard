package com.hammersmith.cammembercard.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Outlet;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class AdapterOutlet extends RecyclerView.Adapter<AdapterOutlet.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Outlet> outlets;
    private Outlet mOutlet;

    public AdapterOutlet(Activity activity, List<Outlet> outlets) {
        this.activity = activity;
        this.outlets = outlets;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_outlet, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        mOutlet = outlets.get(position);
        Uri uri = Uri.parse(ApiClient.BASE_URL + mOutlet.getImage());
        context = holder.imageView.getContext();
        Picasso.with(context).load(uri).into(holder.imageView);
        holder.name.setText(mOutlet.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOutlet = outlets.get(position);
                String strTime = mOutlet.getTimeOpen() + " - " + mOutlet.getTimeClose();
                dialogOutlet(mOutlet.getName(), mOutlet.getAddress(), strTime, mOutlet.getMap(), mOutlet.getFacebook(), mOutlet.getContact(), mOutlet.getWebsite());
            }
        });
    }

    private void dialogOutlet(String name, String address, String time, final String map, final String facebook, final String contact, final String website) {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View viewDialog = factory.inflate(R.layout.dialog_outlet, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        TextView nameLabel, addressLabel, timeLabel;

        nameLabel = (TextView) viewDialog.findViewById(R.id.name);
        addressLabel = (TextView) viewDialog.findViewById(R.id.address);
        timeLabel = (TextView) viewDialog.findViewById(R.id.time);

        nameLabel.setText(name);
        addressLabel.setText(address);
        timeLabel.setText("Open today: " + time);

        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        viewDialog.findViewById(R.id.lMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                activity.startActivity(intent);
            }
        });
        viewDialog.findViewById(R.id.lFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(facebook));
                activity.startActivity(intent);
            }
        });
        viewDialog.findViewById(R.id.lPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));
                activity.startActivity(intent);
            }
        });
        viewDialog.findViewById(R.id.lChrome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(website));
                activity.startActivity(intent);
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return outlets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView imageView;
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
