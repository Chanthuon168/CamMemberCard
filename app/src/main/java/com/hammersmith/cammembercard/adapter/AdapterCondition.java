package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Condition;

import java.util.List;

/**
 * Created by imac on 25/1/17.
 */
public class AdapterCondition extends RecyclerView.Adapter<AdapterCondition.MyViewHolder> {
    private Activity activity;
    private List<Condition> conditions;

    public AdapterCondition(Activity activity, List<Condition> conditions){
        this.activity = activity;
        this.conditions = conditions;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_condition, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.orderNumber.setText(conditions.get(position).getOrderNum()+".");
        holder.condition.setText(conditions.get(position).getCondition());
    }

    @Override
    public int getItemCount() {
        return conditions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, condition;
        public MyViewHolder(View itemView) {
            super(itemView);
            orderNumber = (TextView) itemView.findViewById(R.id.orderNumber);
            condition = (TextView) itemView.findViewById(R.id.condition);
        }
    }
}
