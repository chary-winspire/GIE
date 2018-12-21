package com.example.extarc.androidpushnotification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Constants;
import com.example.extarc.androidpushnotification.Models.UserDetails;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;
import java.util.UUID;

public class LoginRegister extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton GSignin;
    private LoginButton fbLogin;
    private Button fb, google;
    private Button getotp;
    private Button loginEmail;
    private LinearLayout loginwithEmail;
    Context applicationContext;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    String TAG = "LoginRegister";
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    public SharedPreferences preferences;

    private EditText emailID, mobileNo, enterOTP;
    private Button submit;

    private TextView guest;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        applicationContext = getApplicationContext();
        preferences = getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);

        Button loginReg = (Button)findViewById(R.id.loginbtn);
        Button skip =(Button) findViewById(R.id.skipbtn);
        TextView logintext = (TextView)findViewById(R.id.loginText);

        String token = FirebaseInstanceId.getInstance().getToken();
        String deviceID = UUID.randomUUID().toString();
        SharedPreferences  persistentValues = getSharedPreferences(Constants.SP_PERSISTENT_VALUES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistentValues.edit();
        PackageManager manager = applicationContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    applicationContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        Log.d(TAG, "Putting following GCM into SP: " + token);
        editor.putString(Constants.FCM_ID,token);
        editor.putString(Constants.APPLICATION_VERSION_NAME, version);
        editor.putString(Constants.DEVICE_ID,deviceID);
        editor.commit();
        Log.i(TAG, "SharedPreferences PersistentValues File commit: " + persistentValues.getAll().toString());
        storeFCMToken(token,deviceID);


        getotp = findViewById(R.id.getOtp);
        getotp.setOnClickListener(this);

        guest = findViewById(R.id.btnSkip);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegister.this, MasterActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Please Login to get daily Updates", Toast.LENGTH_SHORT).show();
            }
        });

        loginwithEmail = findViewById(R.id.loginwithemail);
        loginEmail = findViewById(R.id.loginEmail);
        loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginwithEmail.isShown()) {
                    loginwithEmail.setVisibility(View.GONE);
                } else {
                    loginwithEmail.setVisibility(View.VISIBLE);
                }
            }
        });

        fb = findViewById(R.id.fb);
        google = findViewById(R.id.google);
        fb.setOnClickListener(this);
        google.setOnClickListener(this);

        emailID = findViewById(R.id.userEmail);
        mobileNo = findViewById(R.id.userMobile);
        enterOTP = findViewById(R.id.enterOtp);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        preferences = getSharedPreferences(Constants.SP_PERSISTENT_VALUES, Context.MODE_PRIVATE);

        callbackManager = CallbackManager.Factory.create();
//
        fbLogin = findViewById(R.id.fbLogin);
        fbLogin.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Callback registration
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.d(TAG, "JsonInputStringer " + currentProfile.getFirstName());

                            AccessToken token = AccessToken.getCurrentAccessToken();

                            if (token != null) {
                                // Token is available, create Graph API request for user info
                                Log.d("AccessToken is ", token.getToken());
                                GraphRequest request = GraphRequest.newMeRequest(token,
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                                if (jsonObject != null) {

                                                    int androidVer = android.os.Build.VERSION.SDK_INT;
                                                    UserDetails userDetails = new UserDetails();
                                                    String fbUserID = jsonObject.optString("id");
                                                    Log.d("User id", jsonObject.toString());
                                                    userDetails.setFgid(fbUserID);
                                                    userDetails.setUserName(jsonObject.optString("name"));
                                                    userDetails.setEmail(jsonObject.optString("email"));
                                                    userDetails.setDateOfBirth(jsonObject.optString("birthday"));
                                                    userDetails.setGender(jsonObject.optString("gender"));
                                                    userDetails.setFirstName(jsonObject.optString("first_name"));
                                                    userDetails.setLastName(jsonObject.optString("last_name"));
                                                    userDetails.setLoginType(Constants.LOGIN_TYPE_FACEBOOK);
                                                    userDetails.setDeviceID(preferences.getString(Constants.DEVICE_ID, ""));
                                                    userDetails.setAndroidVersionType(preferences.getString(Constants.APPLICATION_VERSION_NAME, ""));
                                                    userDetails.setAndroidVersion(androidVer);
                                                    userDetails.setAccountUrl(jsonObject.optString("link"));
                                                    String url = "https://graph.facebook.com/" + jsonObject.optString("id") + "/picture?type=large";
                                                    userDetails.setImageUrl(url);
                                                    String token = FirebaseInstanceId.getInstance().getToken();
                                                    userDetails.setFcmtoken(preferences.getString(Constants.FCM_ID, ""));
                                                    registerUser(userDetails);
                                                }
                                            }
                                        }
                                );
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,link,birthday,email,first_name,last_name,gender");
                                request.setParameters(parameters);
                                request.executeAsync();

                            }
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.d(TAG, "JsonInputStringer " + profile.getFirstName());
                    String deviceID = UUID.randomUUID().toString();
                    int androidVer = android.os.Build.VERSION.SDK_INT;
                    UserDetails userDetails = new UserDetails();
                    String fbUserID = profile.getId();
                    Log.d("User id", profile.getId());
                    userDetails.setFgid(fbUserID);
                    userDetails.setUserName(profile.getName());
                    userDetails.setFirstName(profile.getFirstName());
                    userDetails.setLastName(profile.getLastName());
                    userDetails.setLoginType(Constants.LOGIN_TYPE_FACEBOOK);
                    userDetails.setDeviceID(preferences.getString(Constants.DEVICE_ID, ""));
                    userDetails.setAndroidVersionType(preferences.getString(Constants.APPLICATION_VERSION_NAME, ""));
                    userDetails.setAndroidVersion(androidVer);
                    userDetails.setAccountUrl(profile.getLinkUri().toString());
                    String url = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                    userDetails.setImageUrl(url);

                    userDetails.setFcmtoken(preferences.getString(Constants.FCM_ID, ""));
                    registerUser(userDetails);
                }
            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //  LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        GSignin = findViewById(R.id.googleLogin);
        GSignin.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    private void storeFCMToken(final String FCMToken,final String deviceID) {
        new AsyncTask<Void, Void, String>() {
            String JsonResponse = null;
            @Override
            protected String doInBackground(Void... params) {
                Log.i("getJobDetails", " *** getJobDetails doInBackground started");
                String url =  "insertFcmToken?token=" + FCMToken+"&deviceID="+deviceID;
                try {
                    // Send request to WCF service
                    JsonResponse = WebService.callJsonService(url, "fulljobdesc.bindjobdetails", null, Constants.GET);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.i("getJobDetails", " *** getJobDetails doInBackground completed");
                return JsonResponse;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    SharedPreferences persistentValues = getSharedPreferences(Constants.SP_PERSISTENT_VALUES, Context.MODE_PRIVATE);
                    Log.i(TAG, "Response from server in storeGCMIdInServer: " + msg);
                    if (msg != null) {
                        JSONObject jsoMain = new JSONObject(msg);
                        if (jsoMain.getBoolean("insertFcmTokenResult") == true) {
//                            Log.i(TAG, "GCM details inserted  successfully.");
//                            PackageManager manager = applicationContext.getPackageManager();
//                            PackageInfo info = manager.getPackageInfo(
//                                    applicationContext.getPackageName(), 0);
//                            String version = info.versionName;
//                            SharedPreferences.Editor editor = persistentValues.edit();
//                            // Log.d(TAG, "Putting following GCM into SP: " + userDetails.getGCMId() + version);
//                            //editor.putString(Constants.GCM_ID, userDetails.getGCMId());
//                            editor.putString(Constants.APPLICATION_VERSION_NAME, version);
//
//                            // editor.putString(Constants.DEVICE_ID, userDetails.getAndroidDeviceId());
//
//                            editor.commit();
//                            Log.i(TAG, "SharedPreferences PersistentValues File commit: " + persistentValues.getAll().toString());
                        } else {
                            Log.e(TAG, "Unable to store GCM ID on the Server");
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString(), e);

                }
            }
        }.execute(null, null, null);
    }

    public void HandleResult(GoogleSignInResult result) {
        Log.d(TAG, "googleSignInFailure"+result.isSuccess()+","+result.getStatus());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            Log.d(TAG, "name :"+name);
            String email = account.getEmail();
            String fName = account.getFamilyName();
            String gName = account.getGivenName();
            String token = FirebaseInstanceId.getInstance().getToken();
            String deviceID = UUID.randomUUID().toString();
            int androidVer = android.os.Build.VERSION.SDK_INT;
            String Img_url = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;
            UserDetails userDetails = new UserDetails();
            userDetails.setUserName(name);
            userDetails.setEmail(email);
            userDetails.setDeviceID(preferences.getString(Constants.DEVICE_ID, ""));
            userDetails.setAndroidVersionType(preferences.getString(Constants.APPLICATION_VERSION_NAME, ""));
            userDetails.setAndroidVersion(androidVer);
            userDetails.setLastName(fName);
            userDetails.setMiddleName(gName);
            userDetails.setLoginType(Constants.LOGIN_TYPE_GOOGLE);
            userDetails.setImageUrl(Img_url);
            userDetails.setFcmtoken(preferences.getString(Constants.FCM_ID, ""));
            registerUser(userDetails);
            Log.d(TAG, "googleSignInSuccess");
        } else {
            Log.d(TAG, "googleSignInFailure");
            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            result.getStatus().getStatusMessage();
        }
    }

    public void SignIn() {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, REQ_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "googleSignInFailure"+requestCode);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            HandleResult(result);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
// Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, REQ_CODE);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    googleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
            }
        } else {
            // Show the signed-out UI
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.googleLogin:
                SignIn();
                Intent intent = new Intent(LoginRegister.this, MasterActivity.class);
                startActivity(intent);
                break;
            case R.id.fb:
                fbLogin.performClick();
                break;
            case R.id.google:
                SignIn();
                break;
            case R.id.getOtp:
                enterOTP.setEnabled(true);

                // create a frame layout
                ConstraintLayout replacelayout = new ConstraintLayout(this);
                // set the layout params to fill the activity
                replacelayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                // set an id to the layout
                replacelayout.setId(1000); // some positive integer
                // set the layout as Activity content
                setContentView(replacelayout);
                // Finally , add the fragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(1000, new EntryUserDetailsFragment()).commit();  // 1000 - is the id set for the container layout
                break;
            case R.id.submit:
                loginAttempt();
                break;

        }
    }

    private void registerUser(final UserDetails userDetails) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String url = "registerUser";
                String JsonResponse = null;
                try {
                    Gson gson = new Gson();
                    String userDetailsStr = gson.toJson(userDetails);
                    String SERVICE_URI = ApplicationConstants.SERVER_PATH;
                    JSONStringer JsonInputStringer = new JSONStringer()

                            .object().key("details").value(userDetailsStr)

                            .endObject();
                    Log.d(TAG, "JsonInputStringer " + JsonInputStringer);
                    JsonResponse = WebService.callJsonService(url, TAG + ".saveLeadInfoDetails", JsonInputStringer, Constants.POST);
                    return JsonResponse;
                } catch (Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage(), ex);
                }
                return JsonResponse;
            }

            @Override
            protected void onPostExecute(String msg) {
               /* if(prgDialog.isShowing())
                    prgDialog.hide();*/
                SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.JSON_USERDETAILS, msg);

                editor.commit();

                Intent intent = new Intent(LoginRegister.this, MasterActivity.class);
                startActivity(intent);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              /*  if(!prgDialog.isShowing())
                    prgDialog.show();*/
                Log.d(TAG, "PreExecute Called");
            }
        }.execute(null, null, null);
    }

    private void loginAttempt() {
        // Reset errors.
        emailID.setError(null);
        mobileNo.setError(null);

        // Store values at the time of the login attempt.
        String email = emailID.getText().toString();
        String mobile = mobileNo.getText().toString();

        // Check for a valid Mobile, if the user entered one.
        if (TextUtils.isEmpty(mobile)) {
            mobileNo.setError(getString(R.string.error_mobile_field_required));
        } else if (!isMobileValid(mobile)) {
            mobileNo.setError(getString(R.string.error_invalid_Mobile));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailID.setError(getString(R.string.error_emailid_field_required));
        } else if (!isEmailValid(email)) {
            emailID.setError(getString(R.string.error_invalid_email));
        }
    }

    private boolean isEmailValid(String email) {

        return (email.contains("@") && email.contains(".com"));
    }

    private boolean isMobileValid(String mobile) {

        return mobile.length() == 10;
    }

}


