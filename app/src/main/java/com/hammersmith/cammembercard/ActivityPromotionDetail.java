package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.phenotype.Flag;
import com.hammersmith.cammembercard.adapter.AdapterCondition;
import com.hammersmith.cammembercard.adapter.AdapterDescription;
import com.hammersmith.cammembercard.model.Condition;
import com.hammersmith.cammembercard.model.Description;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.Promotion;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityPromotionDetail extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private AdapterDescription adapter;
    private LinearLayoutManager layoutManager;
    private List<Promotion> cons = new ArrayList<>();
    private Promotion mPro;
    private ImageView imageView, merCard;
    private TextView merName, title, price, proPrice, expDate;
    private Context context;
    private User user;
    private List<MemberCard> memberCards = new ArrayList<>();
    private MemberCard mMember;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_activity_promotion_detail);
        user = PrefUtils.getCurrentUser(ActivityPromotionDetail.this);
        imageView = (ImageView) findViewById(R.id.image);
        merCard = (ImageView) findViewById(R.id.image_card);
        merName = (TextView) findViewById(R.id.merName);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        proPrice = (TextView) findViewById(R.id.proPrice);
        expDate = (TextView) findViewById(R.id.expDate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        findViewById(R.id.redeem).setOnClickListener(this);
        findViewById(R.id.outlet).setOnClickListener(this);

        Intent intent = getIntent();
        mPro = (Promotion) intent.getSerializableExtra("promotion");
        if (mPro != null) {

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<MemberCard>> call = service.getMemberCardByPromotion(mPro.getMerId(), user.getSocialLink());
            call.enqueue(new Callback<List<MemberCard>>() {
                @Override
                public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                    memberCards = response.body();
                    mMember = memberCards.get(0);
                    findViewById(R.id.lCard).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityPromotionDetail.this, DetailActivity.class);
                            intent.putExtra("member", mMember);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<MemberCard>> call, Throwable t) {

                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date toDate = null;
            Date fromDate = null;
            try {
                toDate = format.parse(mPro.getToDate());
                fromDate = format.parse(mPro.getFromDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM yyyy");
            String dateExpire = mFormat.format(toDate);
            String dateFrom = mFormat.format(fromDate);

            Uri uri = Uri.parse(ApiClient.BASE_URL + mPro.getImage());
            context = imageView.getContext();
            Picasso.with(context).load(uri).into(imageView);

            Uri uriCard = Uri.parse(ApiClient.BASE_URL + mPro.getMerPhoto());
            context = merCard.getContext();
            Picasso.with(context).load(uriCard).into(merCard);

            Float oPrice = Float.parseFloat(mPro.getPrice());
            Float savePrice = oPrice * Float.parseFloat(mPro.getDiscount()) / 100;
            Float pPrice = oPrice - savePrice;

            merName.setText(mPro.getMerName());
            title.setText(mPro.getTitle());
            price.setText("USD " + oPrice);
            proPrice.setText("USD " + pPrice);
            expDate.setText(dateFrom + " to " + dateExpire);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });

        recyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ApiInterface serviceCon = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Promotion>> callCon = serviceCon.getProCondition(mPro.getId());
        callCon.enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                cons = response.body();
                adapter = new AdapterDescription(ActivityPromotionDetail.this, cons);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.redeem:
                Toast.makeText(getApplicationContext(),"redeem",Toast.LENGTH_SHORT).show();
                break;
            case R.id.outlet:
                Toast.makeText(getApplicationContext(),"outlets",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
