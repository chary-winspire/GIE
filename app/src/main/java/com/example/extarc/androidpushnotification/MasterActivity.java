package com.example.extarc.androidpushnotification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MasterActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    RelativeLayout mainfragment;
    BottomNavigationView bottomNavigation;
    NavigationView navigationView;
    public static Menu menu_item;
    private String sharePath = "no";
    FloatingActionButton fab;
    private GoogleApiClient googleApiClient;
    Button datepicker, datepicker2;
    TextView toolbartitle;
    private final String TAG = "MasterActivity";

    private File imagePath;
    ImageButton shareit;

    //Permission code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;

    public static final String MIXPANEL_API_TOKEN = "76156b71de6b187d01fa5e920287d4c0";
    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();
        mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_API_TOKEN);
//        // Ensure all future events sent from the device will have the distinct_id 13793
//        mixpanel.identify("13793");
//        // Ensure all future people properties sent from the device will have the distinct_id 13793
//        mixpanel.getPeople().identify("13793");

        trackEvent();
        sentEventWithProperties();

        mixpanel.timeEvent("Image Upload");  // start the timer for the event "Image Upload"
// stop the timer if the imageUpload() method returns true
        if (trackTiming()) {
            mixpanel.track("App Opened");
        }
        setContentView(R.layout.activity_original);

        hideansShowFBLogout();
        hideandShowGLogout();

        shareit = findViewById(R.id.shareMoti);
        shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First checking if the app is already having the permission
                if (isStorageReadable()) {
                    //If permission is already having then showing the toast
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    shareIt();
                    //Existing the method with return
                    mixpanel.track("Sharing Motivation Image");
                    return;
                }
                //If the app has not the permission then asking for the permission
                requestStoragePermission();
//                if (ContextCompat.checkSelfPermission(MasterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //Permission was denied
//                    //Request for permission
//                    ActivityCompat.requestPermissions(MasterActivity.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            1);
//                }
//                if (ContextCompat.checkSelfPermission(MasterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //Permission was denied
//                    //Request for permission
//                    ActivityCompat.requestPermissions(MasterActivity.this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            1);
//                }
                loadMotivation();
            }
        });

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationbar);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigation = findViewById(R.id.bottombar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        toolbartitle = findViewById(R.id.ToolbarTitle);

        datepicker = findViewById(R.id.datePicker);
        datepicker2 = findViewById(R.id.datePicker2);
        long date = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDatef = new SimpleDateFormat("dd MMM yyyy");
        String dateStr = sDatef.format(date);
        datepicker.setText(dateStr);
        datepicker2.setText(dateStr);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorys();
            }
        });

        loadCategorys();

        mainfragment = findViewById(R.id.mainFragment);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    public void trackEvent() {
        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Plan", "Premium");

//            mixpanel.track("Plan Selected", props);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean trackTiming() {

        return true;
    }

    /**
     * Register properties as super properties.
     * Send Properties with event.
     */
    public void sentEventWithProperties() {

        try {
            // Send a "User Type: Paid" property will be sent
            // with all future track calls.
            JSONObject props = new JSONObject();
            props.put("User Type", "Paid");
            mixpanel.registerSuperProperties(props);
            mixpanel.track("setImportant");

        } catch (Exception ignored) {

        }
    }


    public void saveBitmap(Bitmap bitmap) {
        long date = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDatef = new SimpleDateFormat("ddMMyyyy-HH:mm:ss");
        String dateStr = sDatef.format(date);
        imagePath = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getAbsolutePath() + "/" + "Motivation" + "-" + dateStr + ".png"); ////File imagePath
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        Toast.makeText(MasterActivity.this, "Image Saved at: " + imagePath, Toast.LENGTH_SHORT).show();
        mixpanel.track("Saving Motivation Image on Device");
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(R.id.ivMotivation);
//        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        mixpanel.track("Sharing Via");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tShare:
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tProfile:
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tSettings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @SuppressLint("Assert")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bHome:
                loadCategorys();
                mixpanel.track("Categories Page Loading");
                break;

            case R.id.bMotivation:
                loadMotivation();
                mixpanel.track("Motivation Page Loading from BottomBar");
                break;

            case R.id.bPuzzle:
                loadPuzzle();
                mixpanel.track("Puzzle Page Loading from BottomBar");
                break;

            case R.id.bSpdm:
                loadSpeedMaths();
                mixpanel.track("Speed Maths Page Loading from BottomBar");
                break;

            case R.id.bGk:
                loadGK();
                mixpanel.track("GK Page Loading from BottomBar");
                break;

            case R.id.nMotivation:
                loadMotivation();
                mixpanel.track("Motivation Page Loading from Navigation");
                break;

            case R.id.nPuzzle:
                loadPuzzle();
                mixpanel.track("Puzzle Page Loading from Navigation");
                break;

            case R.id.nGk:
                loadGK();
                mixpanel.track("GK Page Loading from Navigation");
                break;

            case R.id.nSpdm:
                loadSpeedMaths();
                mixpanel.track("Speed Maths Page Loading from Navigation");
                break;

            case R.id.nPoll:
                break;

            case R.id.nMchallenge:
                break;

            case R.id.gLogout:
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent GLogout = new Intent(MasterActivity.this, LoginRegister.class);
                        startActivity(GLogout);
                        Toast.makeText(getApplicationContext(), "LoggedOut from Google Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.fbLogout:
                LoginManager.getInstance().logOut();
                Intent FBLogout = new Intent(MasterActivity.this, LoginRegister.class);
                startActivity(FBLogout);
                Toast.makeText(getApplicationContext(), "LoggedOut from Facebook Successfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nShare:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("Share with:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Winspire");
                intent.putExtra(Intent.EXTRA_TEXT, "" + getText(R.string.share_content) + getText(R.string.share_link) + getText(R.string.enjoy_share_content));
                Intent chooser = Intent.createChooser(intent, "Share using");
                startActivity(chooser);
                break;

            case R.id.nSuggest:
                Intent feedback = new Intent(Intent.ACTION_SEND);
                feedback.setData(Uri.parse("mailto:"));
                String[] recipents = {"winspiremagazine@gmail.com"};
                feedback.setType("message/rfc822");
                feedback.putExtra(Intent.EXTRA_EMAIL, recipents);
                feedback.putExtra(Intent.EXTRA_SUBJECT, "Winspire Reviews");
                Intent chooser1 = Intent.createChooser(feedback, "Send Feedback Via");
                startActivity(chooser1);
                break;

            case R.id.naboutApp:
                break;

            case R.id.naboutUs:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment DatePicker = new DatePickerFragment();
        DatePicker.show(getSupportFragmentManager(), "date picker");

    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    //We are calling this method to check the permission status
    private boolean isStorageReadable() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Permission Required to Access Storage", Toast.LENGTH_SHORT).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mixpanel.track("Storage Permission Granted");
                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the Storage permission", Toast.LENGTH_LONG).show();
                mixpanel.track("Storage Permission Denined");
            }
        }
    }

    public void hideandshowToolbar() {

        mainfragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getSupportActionBar().isShowing() && bottomNavigation.isShown()) {
                        getSupportActionBar().hide();
                        bottomNavigation.setVisibility(View.INVISIBLE);
                    } else {
                        getSupportActionBar().show();
                        bottomNavigation.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else return false;
            }
        });
    }

    public void loadCategorys() {
        FragmentManager fmhome = getSupportFragmentManager();
        CategoriesFragment Catefragment = new CategoriesFragment();
        fmhome.beginTransaction().add(R.id.mainFragment, Catefragment).commit();
        FragmentTransaction Ctransaction = getSupportFragmentManager().beginTransaction();
        Ctransaction.replace(R.id.mainFragment, Catefragment);
        Ctransaction.addToBackStack(null);
        Ctransaction.commit();
        shareit.setVisibility(View.INVISIBLE);
        datepicker2.setVisibility(View.INVISIBLE);
        datepicker.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        Objects.requireNonNull(getSupportActionBar()).show();
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void loadMotivation() {
        FragmentManager fmMoti = getSupportFragmentManager();
        MotivationFragment Motifragment = new MotivationFragment();
        fmMoti.beginTransaction().add(R.id.mainFragment, Motifragment).commit();
        FragmentTransaction Mtransaction = getSupportFragmentManager().beginTransaction();
        Mtransaction.replace(R.id.mainFragment, Motifragment);
        Mtransaction.addToBackStack(null);
        Mtransaction.commit();
        shareit.setVisibility(View.VISIBLE);
        datepicker2.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.INVISIBLE);
        datepicker.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bottomNavigation.setVisibility(View.INVISIBLE);
        hideandshowToolbar();
    }

    public void loadPuzzle() {
        FragmentManager fmPuzzle = getSupportFragmentManager();
        PuzzleFragment puzzlefragment = new PuzzleFragment();
        fmPuzzle.beginTransaction().add(R.id.mainFragment, puzzlefragment).commit();
        FragmentTransaction Ptransaction = getSupportFragmentManager().beginTransaction();
        Ptransaction.replace(R.id.mainFragment, puzzlefragment);
        Ptransaction.addToBackStack(null);
        Ptransaction.commit();
        shareit.setVisibility(View.INVISIBLE);
        datepicker2.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.INVISIBLE);
        datepicker.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).show();
        bottomNavigation.setVisibility(View.VISIBLE);
        hideandshowToolbar();
    }

    public void loadGK() {
        FragmentManager fmGK = getSupportFragmentManager();
        GKFragment GKfragment = new GKFragment();
        fmGK.beginTransaction().add(R.id.mainFragment, GKfragment).commit();
        FragmentTransaction GKtransaction = getSupportFragmentManager().beginTransaction();
        GKtransaction.replace(R.id.mainFragment, GKfragment);
        GKtransaction.addToBackStack(null);
        GKtransaction.commit();
        shareit.setVisibility(View.INVISIBLE);
        datepicker2.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.INVISIBLE);
        datepicker.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).show();
        bottomNavigation.setVisibility(View.VISIBLE);
        hideandshowToolbar();
    }

    public void loadSpeedMaths() {
        FragmentManager fmspdm = getSupportFragmentManager();
        SpeedMathsFragment spdmfragment = new SpeedMathsFragment();
        fmspdm.beginTransaction().add(R.id.mainFragment, spdmfragment).commit();
        FragmentTransaction spdmtransaction = getSupportFragmentManager().beginTransaction();
        spdmtransaction.replace(R.id.mainFragment, spdmfragment);
        spdmtransaction.addToBackStack(null);
        spdmtransaction.commit();
        shareit.setVisibility(View.INVISIBLE);
        datepicker2.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.INVISIBLE);
        datepicker.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).show();
        bottomNavigation.setVisibility(View.VISIBLE);
        hideandshowToolbar();
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    private void hideansShowFBLogout() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            navigationView = (NavigationView) findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.fbLogout).setVisible(false);
        } else {
            navigationView = (NavigationView) findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.fbLogout).setVisible(true);
        }
    }

    private void hideandShowGLogout() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            // signed in. Show the "sign out" button and explanation
            navigationView = (NavigationView) findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.gLogout).setVisible(true);

        } else {
            // not signed in. Show the "sign in" button and explanation.
            navigationView = (NavigationView) findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.gLogout).setVisible(false);
        }
    }
}
