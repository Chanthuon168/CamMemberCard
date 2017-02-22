package com.hammersmith.cammembercard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MerchandiseProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_profile);
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
