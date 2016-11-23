package com.hammersmith.cammembercard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hammersmith.cammembercard.adapter.AdapterAlbum;
import com.hammersmith.cammembercard.adapter.ViewPagerAdapter;
import com.hammersmith.cammembercard.model.Album;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mCustomPagerAdapter;
    private List<Album> albums = new ArrayList<>();
    private int id, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        id = getIntent().getIntExtra("id", 0);
        ApiInterface serviceAlbum = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Album>> callAlbum = serviceAlbum.getAlbum(id);
        callAlbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albums = response.body();
                mCustomPagerAdapter = new ViewPagerAdapter(ImageActivity.this, albums, position);
                mViewPager.setAdapter(mCustomPagerAdapter);
                mCustomPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}
