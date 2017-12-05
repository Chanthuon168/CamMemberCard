package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Account;
import com.hammersmith.cammembercard.model.Condition;
import com.hammersmith.cammembercard.model.Description;
import com.hammersmith.cammembercard.model.Promotion;

import java.util.List;

/**
 * Created by imac on 28/3/17.
 */
public class AdapterDescription extends RecyclerView.Adapter<AdapterDescription.MyViewHolder> {
    private List<Promotion> descriptions;
    private Activity activity;

    public AdapterDescription(Activity activity, List<Promotion> descriptions) {
        this.activity = activity;
        this.descriptions = descriptions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_description, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(descriptions.get(position).getCondition());
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.condition);
        }
    }
}
