package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.hammersmith.cammembercard.fragment.FragmentHome;
import com.hammersmith.cammembercard.fragment.FragmentHomeMerchandise;
import com.hammersmith.cammembercard.model.Merchandise;
import com.hammersmith.cammembercard.model.User;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMerchandiseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private View mHeaderView;
    private User user, userSocial;
    private TextView name, email;
    private RoundedImageView profile;
    private Context context = MainMerchandiseActivity.this;
    private FragmentHomeMerchandise fragmentHome;
    private Merchandise merchandise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_merchandise);
        user = PrefUtils.getCurrentUser(MainMerchandiseActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("iMerchandise");
        setSupportActionBar(toolbar);

        initScreen();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);
        name = (TextView) mHeaderView.findViewById(R.id.name);
        email = (TextView) mHeaderView.findViewById(R.id.email);
        profile = (RoundedImageView) mHeaderView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMerchandiseActivity.this, ProfileActivity.class));
            }
        });
        ApiInterface serviceMerchandise = ApiClient.getClient().create(ApiInterface.class);
        Call<Merchandise> callMerchandise = serviceMerchandise.getMerchandise(user.getSocialLink());
        callMerchandise.enqueue(new Callback<Merchandise>() {
            @Override
            public void onResponse(Call<Merchandise> call, Response<Merchandise> response) {
                merchandise = response.body();
                if (merchandise != null) {
                    Uri uri = Uri.parse(merchandise.getPhoto());
                    context = profile.getContext();
                    Picasso.with(context).load(uri).into(profile);
                    name.setText(merchandise.getName());
                    email.setText(merchandise.getEmail());
                }
            }

            @Override
            public void onFailure(Call<Merchandise> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialogExit("Are you sure want to quit the App?");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.scan) {
            startActivity(new Intent(MainMerchandiseActivity.this, ScanQRCodeActivity.class));
        } else if (id == R.id.people_using) {
            startActivity(new Intent(MainMerchandiseActivity.this, ActivityPeopleUsing.class));
        } else if (id == R.id.most_scan) {
            startActivity(new Intent(MainMerchandiseActivity.this, ActivityMostScan.class));
        } else if (id == R.id.today_scan) {
            startActivity(new Intent(MainMerchandiseActivity.this, ActivityTodayScan.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(MainMerchandiseActivity.this, MerchandiseProfile.class));
        } else if (id == R.id.update_account) {
            startActivity(new Intent(MainMerchandiseActivity.this, UpdateProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            PrefUtils.clearCurrentUser(MainMerchandiseActivity.this);
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainMerchandiseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialogExit(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        TextView activate = (TextView) viewDialog.findViewById(R.id.ok);
        activate.setText("Exit");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        viewDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initScreen() {
        fragmentHome = new FragmentHomeMerchandise();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.continer_framelayout, fragmentHome).commit();
    }
}
