package com.hammersmith.cammembercard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hammersmith.cammembercard.model.User;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String nameFb, emailFb, linkFb, nameGoogle, emailGoogle, linkGoogle, profileGoogle, strProfile;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private User user;
    private EditText name, email, password, confirmPassword;
    private String strName, strEmail, strPassword, strConfirmPassword, strPhoto;
    private View view;
    private ProgressDialog mProgressDialog;

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
                Toast.makeText(RegisterActivity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_register);
        view = findViewById(R.id.lRegister);
        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.btnFb).setOnClickListener(this);
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        buildGoogleApiClient(null);
    }

    private void buildGoogleApiClient(String accountName) {
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile();

        if (accountName != null) {
            gsoBuilder.setAccountName(accountName);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(RegisterActivity.this);
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(RegisterActivity.this)
                .enableAutoManage(RegisterActivity.this, this)
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
            user = new User();
            user.setName(nameGoogle);
            user.setEmail(emailGoogle);
            user.setSocialLink(linkGoogle);
            user.setPhoto(profileGoogle);
            PrefUtils.setCurrentUser(user, RegisterActivity.this);
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
                        user = new User();
                        user.setName(nameFb);
                        user.setEmail(emailFb);
                        user.setSocialLink(linkFb);
                        user.setPhoto(photo);
                        PrefUtils.setCurrentUser(user, RegisterActivity.this);
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

    private void saveUserSocial(String name, String email, String photo, String socialLink, String socialType) {
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
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
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
            case R.id.signIn:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
                break;
            case R.id.btnFb:
                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                break;
            case R.id.btnGoogleSignIn:
                signIn();
                break;
            case R.id.register:
                strName = name.getText().toString();
                strEmail = email.getText().toString();
                strPassword = password.getText().toString();
                strConfirmPassword = confirmPassword.getText().toString();
                strPhoto = ApiClient.BASE_URL + "images/user.png";
                if (strName.equals("") || strName.length() < 6) {
                    Snackbar snackbar = Snackbar.make(view, "Username at least 6 characters", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!isEmailValid(strEmail)) {
                    Snackbar snackbar = Snackbar.make(view, "Email is incorrect", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!strPassword.equals(strConfirmPassword)) {
                    Snackbar snackbar = Snackbar.make(view, "Password is not match", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    userRegister(strName, strEmail, strPassword, strPhoto);
                }
                break;
        }
    }

    private void userRegister(String name, String email, String password, String photo) {
        showProgressDialog();
        user = new User(name, email, password, photo);
        ApiInterface serviceRegister = ApiClient.getClient().create(ApiInterface.class);
        Call<User> callRegister = serviceRegister.userRegister(user);
        callRegister.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                hideProgressDialog();
                if (user.getMsg().equals("Account has been registered")) {
                    Intent intentNew = new Intent(RegisterActivity.this, LoginActivity.class);
                    intentNew.putExtra("isNewItem", "register");
                    intentNew.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intentNew);
                    finish();
                } else {
                    dialogMessage(user.getMsg());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                dialogError("An occur while being register, try again");
                hideProgressDialog();
            }
        });
    }

    private void dialogError(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        IconTextView icon = (IconTextView) viewDialog.findViewById(R.id.icon);
        icon.setText("{fa-times-circle}");
        viewDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister(strName, strEmail, strPassword, strPhoto);
                dialog.dismiss();
            }
        });
        viewDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogMessage(String strMessage) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.cancel).setVisibility(View.GONE);
        TextView message = (TextView) viewDialog.findViewById(R.id.message);
        message.setText(strMessage);
        IconTextView icon = (IconTextView) viewDialog.findViewById(R.id.icon);
        icon.setText("{fa-times-circle}");
        viewDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
