package com.hammersmith.cammembercard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hammersmith.cammembercard.model.ForgotPassword;
import com.hammersmith.cammembercard.model.User;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String nameFb, emailFb, linkFb, nameGoogle, emailGoogle, linkGoogle, profileGoogle, strProfile;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    private User user, userPref;
    private String strEmail, strPassword;
    private EditText email, password;
    private String strLoginAs = "isUser";
    private AlertDialog dialogForgot;
    private CheckBox checkbox;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ForgotPassword forgotPassword;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Log.d("Success","Login");
                RequestData();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.btnFb).setOnClickListener(this);
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(this);
        findViewById(R.id.l_login).setOnClickListener(this);
        findViewById(R.id.txtForgetPass).setOnClickListener(this);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    strLoginAs = "isMerchandise";
                } else {
                    strLoginAs = "isUser";
                }
            }
        });

        buildGoogleApiClient(null);

        if (PrefUtils.getCurrentUser(LoginActivity.this) != null) {
            user = PrefUtils.getCurrentUser(LoginActivity.this);
            if (user.getLoginAs().equals("isMerchandise")) {
                Intent intentMer = new Intent(LoginActivity.this, MainMerchandiseActivity.class);
                startActivity(intentMer);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("isNewItem")) {
                boolean isNew = extras.getBoolean("isNewItem", false);
                if (!isNew) {
                    dialogMessage("Account has been registered successfully! Please activate account on your email.");
                }
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        printKeyHash(LoginActivity.this);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void dialogMessage(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        TextView cancel = (TextView) viewDialog.findViewById(R.id.cancel);
        TextView ok = (TextView) viewDialog.findViewById(R.id.ok);
        if (strMessage.equals("Email or password is incorrect")) {
            cancel.setText("Close");
            ok.setVisibility(View.GONE);
        } else {
            cancel.setText("Close");
            ok.setText("Activate");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    String url = "https://mail.google.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogActivate(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        TextView activate = (TextView) viewDialog.findViewById(R.id.ok);
        activate.setText("Activate");
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mail.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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

    private void buildGoogleApiClient(String accountName) {
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile();

        if (accountName != null) {
            gsoBuilder.setAccountName(accountName);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(LoginActivity.this);
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build());

        mGoogleApiClient = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Google Login", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            nameGoogle = acct.getDisplayName();
            emailGoogle = acct.getEmail();
            linkGoogle = acct.getId();
            profileGoogle = String.valueOf(acct.getPhotoUrl());
            Log.d("googleData", nameGoogle + emailGoogle + linkGoogle);
            if (profileGoogle.equals("null")) {
                profileGoogle = ApiClient.BASE_URL + "images/user.png";
            }
            saveUserSocial(nameGoogle, emailGoogle, profileGoogle, linkGoogle, "gg");
            signOut();
        } else {
            Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btnGoogleSignIn).setVisibility(View.GONE);
            findViewById(R.id.btnGoogleSignOut).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btnGoogleSignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.btnGoogleSignOut).setVisibility(View.GONE);
        }
    }

    private void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                if (json != null) {
                    try {
                        nameFb = json.getString("name");
                        emailFb = json.getString("email");
                        linkFb = json.getString("id");
                        Log.d("facebookData", nameFb + emailFb + linkFb);
                        String photo = "https://graph.facebook.com/" + linkFb + "/picture?type=large";
                        saveUserSocial(nameFb, emailFb, photo, linkFb, "fb");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveUserSocial(final String name, final String email, final String photo, final String socialLink, final String socialType) {
        showProgressDialog();
        user = new User(name, email, photo, socialLink, socialType);
        ApiInterface serviceUserLogin = ApiClient.getClient().create(ApiInterface.class);
        Call<User> callLogin = serviceUserLogin.createUserBySocial(user);
        callLogin.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if (user != null) {
                    if (user.getMsg().equals("success")) {
                        String photo = user.getPhoto();
                        user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        user.setSocialLink(socialLink);
                        user.setPhoto(photo);
                        user.setLoginAs("isUser");
                        user.setSocialType(socialType);
                        PrefUtils.setCurrentUser(user, LoginActivity.this);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
                break;
            case R.id.btnFb:
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                break;
            case R.id.btnGoogleSignIn:
                signIn();
                break;
            case R.id.l_login:
                strEmail = email.getText().toString();
                strPassword = password.getText().toString();
                loginByEmail(strLoginAs, strEmail, strPassword);
                break;
            case R.id.txtForgetPass:
                dialogForgotPass();
                break;
        }
    }

    private void loginByEmail(String loginAs, String email, String password) {
        showProgressDialog();
        user = new User(loginAs, email, password);
        ApiInterface serviceLoginByEmail = ApiClient.getClient().create(ApiInterface.class);
        Call<User> callLoginByEmail = serviceLoginByEmail.userLoginByEmail(user);
        callLoginByEmail.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if (user.getMsg().equals("available")) {
                    userPref = new User();
                    userPref.setName(user.getName());
                    userPref.setEmail(user.getEmail());
                    userPref.setSocialLink(user.getSocialLink());
                    userPref.setPhoto(user.getPhoto());
                    userPref.setLoginAs(user.getLoginAs());
                    userPref.setSocialType(user.getSocialType());
                    PrefUtils.setCurrentUser(userPref, LoginActivity.this);
                    if (user.getLoginAs().equals("isMerchandise")) {
                        Intent intent = new Intent(LoginActivity.this, MainMerchandiseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                } else if (user.getMsg().equals("Account haven't verify yet")) {
                    dialogActivate("Account haven't verify yet");
                } else {
                    dialogMessage(user.getMsg());
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }

    private void dialogForgotPass() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_forgot_password, null);
        dialogForgot = new AlertDialog.Builder(this).create();
        dialogForgot.setView(viewDialog);
        final EditText edEmail = (EditText) viewDialog.findViewById(R.id.email);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForgot.dismiss();
            }
        });
        viewDialog.findViewById(R.id.lConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                if (email.equals("")) {
                    dialog("Please enter your email address");
                } else {
                    showProgressDialog();
                    forgotPassword = new ForgotPassword(email);
                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<ForgotPassword> call = service.forgotPassword(forgotPassword);
                    call.enqueue(new Callback<ForgotPassword>() {
                        @Override
                        public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                            hideProgressDialog();
                            forgotPassword = response.body();
                            if (forgotPassword != null) {
                                if (forgotPassword.getMsg().equals("Please check your email new password was sent")) {
                                    dialogForgot.dismiss();
                                }
                                dialog(forgotPassword.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPassword> call, Throwable t) {
                            hideProgressDialog();
                            dialog("Please check your internet connection");
                        }
                    });
                }
            }
        });

        dialogForgot.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Processing...");
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

    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.hammersmith.cammembercard/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.hammersmith.cammembercard/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private void dialog(String strMessage) {
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
