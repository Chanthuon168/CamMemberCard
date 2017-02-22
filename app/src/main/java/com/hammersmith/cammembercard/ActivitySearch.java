package com.hammersmith.cammembercard;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.hammersmith.cammembercard.adapter.AdapterMemberCard;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySearch extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editText;
    private ImageView clearText;
    private ProgressDialog mProgressDialog;
    private String str_search;


    private RecyclerView recyclerView;
    private AdapterMemberCard adapter;
    private LinearLayoutManager layoutManager;
    private List<MemberCard> members = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_search);
        user = PrefUtils.getCurrentUser(ActivitySearch.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        clearText = (ImageView) findViewById(R.id.search_clear);
        editText = (EditText) findViewById(R.id.editText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    str_search = editText.getText().toString();
//                    str_search = str_search.replaceAll(" ", "_").toLowerCase();
                    showProgressDialog();
                    ApiInterface serviceFilter = ApiClient.getClient().create(ApiInterface.class);
                    Call<List<MemberCard>> callFilter = serviceFilter.filterByName(user.getSocialLink(),str_search);
                    callFilter.enqueue(new Callback<List<MemberCard>>() {
                        @Override
                        public void onResponse(Call<List<MemberCard>> call, Response<List<MemberCard>> response) {
                            members = response.body();
                            hideProgressDialog();
                            if (members.size() < 1){
                                findViewById(R.id.txtFilter).setVisibility(View.VISIBLE);
                                adapter = new AdapterMemberCard(ActivitySearch.this, members);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else{
                                findViewById(R.id.txtFilter).setVisibility(View.GONE);
                                adapter = new AdapterMemberCard(ActivitySearch.this, members);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MemberCard>> call, Throwable t) {

                        }
                    });
                    return true;
                }
                return false;
            }
        });

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ActivitySearch.this);
            mProgressDialog.setMessage("Filtering...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
