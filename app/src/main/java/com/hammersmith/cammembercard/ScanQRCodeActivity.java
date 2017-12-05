package com.hammersmith.cammembercard;

import android.app.ProgressDialog;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hammersmith.cammembercard.model.Merchandise;
import com.hammersmith.cammembercard.model.Scanned;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScanQRCodeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtDiscount;
    private User user;
    private Merchandise merchandise;
    private RoundedImageView profile;
    private TextView name;
    private Context context;
    private NestedScrollView mainBody;
    private LinearLayout lScan;
    private SwipeRefreshLayout swipeRefresh;
    private Scanned scan;
    private String currentDateTime;
    private RatingBar ratingBar;
    private EditText amount;
    private ProgressDialog mProgressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        amount = (EditText) findViewById(R.id.amount);

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
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });

        lScan = (LinearLayout) findViewById(R.id.lScan);
        lScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().equals("")) {
                    dialogExit("Please enter sub total (USD)");
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    currentDateTime = dateFormat.format(new Date());
                    txtDiscount.setText("");
                    IntentIntegrator integrator = new IntentIntegrator(ScanQRCodeActivity.this);
                    integrator.setPrompt("Please QR Code inside the viewfinder rectangle");
                    integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                    showProgressDialog();
                }
            }
        });

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
                    ratingBar.setRating(Float.parseFloat(merchandise.getRating()));
                    lScan.setVisibility(View.VISIBLE);
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
                mProgressDialog.hide();
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
                            postScanned(amount.getText().toString(), socialLink, user.getSocialLink(), merId, discount, currentDateTime);
//                            txtDiscount.setText(discount + "% OFF");
                        } else {
//                            txtDiscount.setText("");
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

    private void postScanned(String amount, String userLink, final String scannerLink, int merId, String discount, String createdAt) {
        scan = new Scanned(amount, userLink, scannerLink, merId, discount, createdAt);
        ApiInterface serviceScan = ApiClient.getClient().create(ApiInterface.class);
        Call<Scanned> callScan = serviceScan.postScan(scan);
        callScan.enqueue(new Callback<Scanned>() {
            @Override
            public void onResponse(Call<Scanned> call, Response<Scanned> response) {
                scan = response.body();
                hideProgressDialog();
                if (scan.getStatus() != null) {
                    if (scan.getStatus().equals("Success")) {
                        dialogResultSuccess(scan);
                    } else {
                        dialogResultFail(scan.getStatus(), scan.getSmg());
                    }
                } else {
                    dialogResultFail("Fail", "Please try again!");
                }
            }

            @Override
            public void onFailure(Call<Scanned> call, Throwable t) {
                hideProgressDialog();
                dialogResultFail("Fail", "Please try again!");
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

    private void dialogResultFail(String strTitle, String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog_result_fail, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView title = (TextView) viewDialog.findViewById(R.id.title);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        title.setText(strTitle);
        TextView activate = (TextView) viewDialog.findViewById(R.id.cancel);
        activate.setText("OK");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogResultSuccess(Scanned scan) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog_result_success, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView title = (TextView) viewDialog.findViewById(R.id.title);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        TextView total = (TextView) viewDialog.findViewById(R.id.total);
        TextView discount = (TextView) viewDialog.findViewById(R.id.discount);
        TextView save = (TextView) viewDialog.findViewById(R.id.save);
        TextView pay = (TextView) viewDialog.findViewById(R.id.pay);
        total.setText("Total: $" + amount.getText().toString());
        discount.setText("Discount: " + scan.getDiscount() + "%");
        save.setText("Saved: $" + scan.getSave());
        pay.setText("Pay only: $" + scan.getPaid());
        message.setText(scan.getSmg());
        title.setText(scan.getStatus());
        TextView activate = (TextView) viewDialog.findViewById(R.id.cancel);
        activate.setText("OK");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                amount.setText("");
            }
        });

        dialog.show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Scanning...");
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
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
