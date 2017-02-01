package com.hammersmith.cammembercard;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RoundedImageView profile;
    private User user;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = PrefUtils.getCurrentUser(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile = (RoundedImageView) findViewById(R.id.profile);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Uri uri = Uri.parse(user.getPhoto());
        context = profile.getContext();
        Picasso.with(context).load(uri).into(profile);
    }
}
