package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hammersmith.cammembercard.adapter.AdapterPeopleUsing;
import com.hammersmith.cammembercard.model.Merchandise;
import com.hammersmith.cammembercard.model.Scanned;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRCodeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtDiscount;
    private User user;
    private Merchandise merchandise;
    private RoundedImageView profile;
    private TextView name;
    private Context context;
    private NestedScrollView mainBody;
    private ImageView imgScan;
    private SwipeRefreshLayout swipeRefresh;
    private Scanned scan;
    private String currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqr_code);
        user = PrefUtils.getCurrentUser(ScanQRCodeActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtDiscount = (TextView) findViewById(R.id.discount);
        profile = (RoundedImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        mainBody = (NestedScrollView) findViewById(R.id.mainBody);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = dateFormat.format(new Date());

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);

        toolbar.setTitle("Scan QRCode");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgScan = (ImageView) findViewById(R.id.imgScan);
        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDiscount.setText("");
                IntentIntegrator integrator = new IntentIntegrator(ScanQRCodeActivity.this);
                integrator.setPrompt("Please QR Code inside the viewfinder rectangle");
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//        integrator.setPrompt("Scan a barcode");
//        integrator.setCameraId(0);  // Use a specific camera of the device
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.initiateScan();

        ApiInterface serviceMerchandise = ApiClient.getClient().create(ApiInterface.class);
        Call<Merchandise> callMerchandise = serviceMerchandise.getMerchandise(user.getSocialLink());
        callMerchandise.enqueue(new Callback<Merchandise>() {
            @Override
            public void onResponse(Call<Merchandise> call, Response<Merchandise> response) {
                merchandise = response.body();
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);
                if (merchandise != null) {
                    Uri uri = Uri.parse(ApiClient.BASE_URL + merchandise.getMerPhoto());
                    context = profile.getContext();
                    Picasso.with(context).load(uri).into(profile);
                    name.setText(merchandise.getMerName());
                    imgScan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Merchandise> call, Throwable t) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String dataJson = result.getContents();
                Log.d("dataJson", dataJson);
                try {
                    JSONObject obj = new JSONObject(dataJson);
                    if (obj != null) {
                        Log.d("mer_id", merchandise.getMerId() + "");
                        String socialLink = obj.getString("id");
                        int merId = obj.getInt("mer_id");
                        String discount = obj.getString("discount");
                        if (merId == merchandise.getMerId()) {
                            postScanned(socialLink, user.getSocialLink(), merId, discount, currentDateTime);
                            txtDiscount.setText(discount + "% OFF");
                        } else {
                            txtDiscount.setText("");
                            Snackbar snackbar = Snackbar.make(mainBody, "Sorry this QR Code can not readable", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } else {
                        Toast.makeText(this, "Sorry data dose not valid", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void postScanned(String userLink, String scannerLink, int merId, String discount, String createdAt) {
        scan = new Scanned(userLink, scannerLink, merId, discount, createdAt);
        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<Scanned> callScan = serviceScan.postScan(scan);
        callScan.enqueue(new Callback<Scanned>() {
            @Override
            public void onResponse(Call<Scanned> call, Response<Scanned> response) {
                scan = response.body();
                if (scan.getSmg() != null) {
                    dialogExit(scan.getSmg());
                } else {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Scanned> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogExit(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        viewDialog.findViewById(R.id.cancel).setVisibility(View.GONE);
        message.setText(strMessage);
        TextView activate = (TextView) viewDialog.findViewById(R.id.ok);
        activate.setText("Close");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
