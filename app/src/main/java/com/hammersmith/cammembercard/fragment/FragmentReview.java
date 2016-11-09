package com.hammersmith.cammembercard.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterReview;
import com.hammersmith.cammembercard.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentReview extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Review> reviews = new ArrayList<>();
    private AdapterReview adapterReview;
    private LinearLayoutManager layoutManager;
    private EditText edComment;
    private ImageView iconSend;

    public FragmentReview() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        iconSend = (ImageView) view.findViewById(R.id.iconSend);
        edComment = (EditText) view.findViewById(R.id.edComment);
        iconSend.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        adapterReview = new AdapterReview(getActivity(), reviews);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterReview);

        edComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    iconSend.setEnabled(false);
                    iconSend.setImageResource(R.drawable.unsend);
                } else {
                    iconSend.setEnabled(true);
                    iconSend.setImageResource(R.drawable.send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iconSend:
                Toast.makeText(getActivity(), edComment.getText().toString(), Toast.LENGTH_SHORT).show();
                edComment.setText("");
                break;
        }
    }
}
