package com.hammersmith.cammembercard;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hammersmith.cammembercard.model.Account;
import com.hammersmith.cammembercard.model.User;
import com.joanzapata.iconify.widget.IconTextView;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private Toolbar toolbar;
    private RoundedImageView profile, camera;
    private Context context;
    private EditText name, gender, contact, email, country, address, dob;
    private User user, userSocial, mUser;
    private SwipeRefreshLayout swipeRefresh;
    private String strGender;

    private ArrayList<String> imageList = new ArrayList<>();
    private static final int SELECT_PHOTO = 100;
    private GalleryPhoto galleryPhoto;
    private static String photoPath;
    private static String encoded;
    private Account account;
    private String userLink;
    private TextView resetPassword;
    private AlertDialog dialogResetPass;

    private ProgressDialog mProgressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        user = PrefUtils.getCurrentUser(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile = (RoundedImageView) findViewById(R.id.profile);
        name = (EditText) findViewById(R.id.name);
        gender = (EditText) findViewById(R.id.gender);
        contact = (EditText) findViewById(R.id.contact);
        email = (EditText) findViewById(R.id.email);
        country = (EditText) findViewById(R.id.country);
        address = (EditText) findViewById(R.id.address);
        dob = (EditText) findViewById(R.id.dob);
        camera = (RoundedImageView) findViewById(R.id.camera);
        resetPassword = (TextView) findViewById(R.id.resetPassword);
        camera.setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        gender.setOnFocusChangeListener(this);
        dob.setOnFocusChangeListener(this);
        email.setFocusable(false);
        resetPassword.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.yellow);

        toolbar.setTitle("Update Account");
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

        if (user.getSocialType().equals("ac")) {
            resetPassword.setVisibility(View.VISIBLE);
        }

        galleryPhoto = new GalleryPhoto(this);

        userLink = user.getSocialLink();

        userSocial = new User(user.getSocialLink());
        ApiInterface serviceUser = ApiClient.getClient().create(ApiInterface.class);
        Call<User> callUser = serviceUser.getUser(userSocial);
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setEnabled(false);
                camera.setVisibility(View.VISIBLE);
                user = response.body();
                name.setText(user.getName());
                email.setText(user.getEmail());
                Uri uri = Uri.parse(user.getPhoto());
                context = profile.getContext();
                Picasso.with(context).load(uri).into(profile);
                if (user.getGender() != null) {
                    gender.setText(user.getGender());
                } else {
                    gender.setText("None");
                }
                if (user.getDateOfBirth() != null) {
                    dob.setText(user.getDateOfBirth());
                } else {
                    dob.setText("None");
                }
                if (user.getContact() != null) {
                    contact.setText(user.getContact());
                } else {
                    contact.setText("None");
                }
                if (user.getAddress() != null) {
                    address.setText(user.getAddress());
                } else {
                    address.setText("None");
                }
                if (user.getCountry() != null) {
                    country.setText(user.getCountry());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                showProgressDialog("Account updating...");
                if (imageList.size() < 1) {
                    updateUser();
                } else {
                    uploadFile();
                }
                break;
            case R.id.camera:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.resetPassword:
                dialogResetPassword();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.gender:
                if (hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final CharSequence[] array = {"Male", "Female"};
                    builder.setTitle("Gender")
                            .setSingleChoiceItems(array, 2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int position) {
                                    strGender = (String) array[position];
                                }
                            })

                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    gender.setText(strGender);
                                    gender.setCursorVisible(false);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    gender.setCursorVisible(false);
                                }
                            });

                    builder.show();
                }
                break;
            case R.id.dob:
                if (hasFocus) {
                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "dd MMM yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            dob.setText(sdf.format(myCalendar.getTime()));
                            dob.setCursorVisible(false);

                        }

                    };
                    new DatePickerDialog(UpdateProfileActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageList.clear();
        if (requestCode == SELECT_PHOTO && resultCode == this.RESULT_OK) {
            galleryPhoto.setPhotoUri(data.getData());
            photoPath = galleryPhoto.getPath();
            imageList.add(photoPath);
            Log.d("path", photoPath);
            try {
                Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                profile.setImageBitmap(bitmap);
                encoded = getEncoded64ImageStringFromBitmap(bitmap);
//                Log.d("strBase64", encoded);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Error while loading image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    private void dialogSuccess(String strMessage, String strButton) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        TextView cancel = (TextView) viewDialog.findViewById(R.id.cancel);
        viewDialog.findViewById(R.id.ok).setVisibility(View.GONE);
        cancel.setText(strButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
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

    private void updateUser() {
        String strName = name.getText().toString();
        String strGender = gender.getText().toString();
        String strPhone = contact.getText().toString();
        String strCountry = country.getText().toString();
        String strAddress = address.getText().toString();
        String strDob = dob.getText().toString();
        account = new Account(userLink, user.getPhoto(), strName, strGender, strPhone, strCountry, strAddress, strDob);
        ApiInterface serviceAccount = ApiClient.getClient().create(ApiInterface.class);
        Call<Account> callAccount = serviceAccount.updateAccount(account);
        callAccount.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                hideProgressDialog();
                imageList.clear();
                account = response.body();
                if (account.getMsg().equals("available")) {
                    mUser = new User();
                    mUser.setSocialLink(account.getUserLink());
                    mUser.setPhoto(account.getPhoto());
                    mUser.setEmail(account.getEmail());
                    mUser.setName(account.getName());
                    mUser.setLoginAs("isUser");
                    mUser.setSocialType(account.getSocialType());
                    PrefUtils.setCurrentUser(mUser, UpdateProfileActivity.this);
                    dialogSuccess("Account has been updated", "Success");
                } else {
                    dialogSuccess("Error while updating account", "Fail");
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    private void uploadFile() {
        final String strName = name.getText().toString();
        final String strGender = gender.getText().toString();
        final String strPhone = contact.getText().toString();
        final String strCountry = country.getText().toString();
        final String strAddress = address.getText().toString();
        final String strDob = dob.getText().toString();
        account = new Account(encoded);
        ApiInterface serviceUploadFile = ApiClient.getClient().create(ApiInterface.class);
        Call<Account> callUploadFile = serviceUploadFile.uploadFile(account);
        callUploadFile.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                account = response.body();
                if (account.getMsg().equals("uploaded_failed")) {
                    hideProgressDialog();
                    dialogSuccess("Error while uploading profile account", "Error");
                } else {
                    Log.d("userLink", userLink + "");
                    String strPhoto = ApiClient.BASE_URL + "images/" + account.getMsg();
                    Log.d("strPhoto", strPhoto);
                    account = new Account(userLink, strPhoto, strName, strGender, strPhone, strCountry, strAddress, strDob);
                    ApiInterface serviceAccount = ApiClient.getClient().create(ApiInterface.class);
                    Call<Account> callAccount = serviceAccount.updateAccount(account);
                    callAccount.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            hideProgressDialog();
                            imageList.clear();
                            account = response.body();
                            if (account.getMsg().equals("available")) {
                                mUser = new User();
                                mUser.setSocialLink(account.getUserLink());
                                mUser.setPhoto(account.getPhoto());
                                mUser.setEmail(account.getEmail());
                                mUser.setName(account.getName());
                                mUser.setLoginAs("isUser");
                                mUser.setSocialType(account.getSocialType());
                                PrefUtils.setCurrentUser(mUser, UpdateProfileActivity.this);
                                dialogSuccess("Account has been updated", "Success");
                            } else {
                                dialogSuccess("Error while updating account", "Fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                dialogSuccess("Error while uploading profile account", "Fail");
                hideProgressDialog();
            }
        });
    }

    private void dialogResetPassword() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog_reset_password, null);
        dialogResetPass = new AlertDialog.Builder(this).create();
        dialogResetPass.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResetPass.dismiss();
            }
        });
        final EditText edCurrPass, edNewPass, edConPass;
        edCurrPass = (EditText) viewDialog.findViewById(R.id.currentPassword);
        edNewPass = (EditText) viewDialog.findViewById(R.id.newPassword);
        edConPass = (EditText) viewDialog.findViewById(R.id.confirmPassword);
        viewDialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCurrPass, strNewPass, strConPass;
                strCurrPass = edCurrPass.getText().toString();
                strNewPass = edNewPass.getText().toString();
                strConPass = edConPass.getText().toString();
                if (!strCurrPass.equals("")) {
                    if (!strNewPass.equals("")) {
                        if (!strNewPass.equals(strConPass)) {
                            dialogSuccess("Password is not match", "Close");
                        } else {
                            showProgressDialog("Password is resetting");
                            resetPassword(userLink, strCurrPass, strNewPass);
                        }
                    } else {
                        dialogSuccess("New password must be not null", "Close");
                    }
                } else {
                    dialogSuccess("Current password must be not null", "Close");
                }
            }
        });

        dialogResetPass.show();
    }

    private void resetPassword(String userLink, String currentPass, String newPass) {
        account = new Account(userLink, currentPass, newPass);
        ApiInterface serviceResetPass = ApiClient.getClient().create(ApiInterface.class);
        Call<Account> userCall = serviceResetPass.resetUserPassword(account);
        userCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                account = response.body();
                hideProgressDialog();
                if (account != null) {
                    if (account.getMsg().equals("New password has been saved")) {
                        dialogResetPass.dismiss();
                        dialogSuccess(account.getMsg(), "Success");
                    } else {
                        dialogSuccess(account.getMsg(), "Close");
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
