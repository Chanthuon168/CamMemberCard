package com.hammersmith.cammembercard.fragment;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
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

import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.ApiInterface;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterReview;
import com.hammersmith.cammembercard.model.Review;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private User user;
    private Review review;
    private int merId;
    private String currentDateTime;
    private LinearLayout lNoReview;
    private ImageView profile;
    private Context context;

    public FragmentReview() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        user = PrefUtils.getCurrentUser(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        iconSend = (ImageView) view.findViewById(R.id.iconSend);
        edComment = (EditText) view.findViewById(R.id.edComment);
        lNoReview = (LinearLayout) view.findViewById(R.id.lNoReview);
        profile = (ImageView) view.findViewById(R.id.profile);
        iconSend.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = dateFormat.format(new Date());
        DetailActivity activity = (DetailActivity) getActivity();
        merId = activity.getMyData();
        getComment(merId);
        Uri uri = Uri.parse(user.getPhoto());
        context = profile.getContext();
        Picasso.with(context).load(uri).into(profile);
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
                postReview(merId, user.getSocialLink(), edComment.getText().toString(), currentDateTime);
                edComment.setText("");
                break;
        }
    }

    private void postReview(final int merId, String userLink, String comment, String createdAt) {
        review = new Review(merId, userLink, comment, createdAt);
        ApiInterface serviceReview = ApiClient.getClient().create(ApiInterface.class);
        Call<Review> callReview = serviceReview.postReview(review);
        callReview.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                review = response.body();
                if (review.getMsg().equals("new")) {
                    getComment(merId);
                } else {
                    getActivity().findViewById(R.id.lNoReview).setVisibility(View.GONE);
                    Review rev = new Review();
                    rev.setProfile(review.getProfile());
                    rev.setName(review.getName());
                    rev.setComment(review.getComment());
                    rev.setCreatedAt(review.getCreatedAt());
                    reviews.add(rev);
                    adapterReview.notifyDataSetChanged();
                    if (adapterReview.getItemCount() > 1) {
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapterReview.getItemCount() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComment(int merId) {
        ApiInterface serviceReview = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Review>> callReview = serviceReview.getReview(merId);
        callReview.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviews = response.body();
                if (reviews.size() > 0) {
                    adapterReview = new AdapterReview(getActivity(), reviews);
                    recyclerView.setAdapter(adapterReview);
                    adapterReview.notifyDataSetChanged();
                    lNoReview.setVisibility(View.GONE);
                } else {
                    lNoReview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }
}
