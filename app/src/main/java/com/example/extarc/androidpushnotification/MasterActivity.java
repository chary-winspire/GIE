package com.example.extarc.androidpushnotification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
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

import com.example.extarc.androidpushnotification.Models.UserDetails;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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
import java.util.jar.Attributes;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MasterActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    RelativeLayout mainfragment;
    public static BottomNavigationView bottomNavigation;
    public static Toolbar toolbar;
    public static AppBarLayout appBarLayout;
    public static NavigationView navigationView;
    public static DrawerLayout drawerLayout;
    public  static FloatingActionButton fab;
    public static TextView toolbartitle2;
    public static TextView toolbartitle;
    private GoogleApiClient googleApiClient;
    public static Button datepicker, datepicker2;
    private final String TAG = "MasterActivity";

    UserDetails userDetails;

    private File imagePath;
    public static ImageButton shareit;

    Button FBLogout, GLogout, SignIn;

    //Permission code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;

    public static final String MIXPANEL_API_TOKEN = "76156b71de6b187d01fa5e920287d4c0";
    MixpanelAPI mixpanel;

    String name, email, Name, Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestStoragePermission();
        mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_API_TOKEN);
        trackEvent();
        sentEventWithProperties();

        mixpanel.timeEvent("Image Upload");  // start the timer for the event "Image Upload"
// stop the timer if the imageUpload() method returns true
        if (trackTiming()) {
            mixpanel.track("App Opened");
        }
        setContentView(R.layout.activity_original);

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
                loadMotivation();
            }
        });

        toolbartitle = findViewById(R.id.ToolbarTitle);
        toolbartitle2 = findViewById(R.id.ToolbarTitle2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        toolbartitle2.setVisibility(View.GONE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle.setText("Winspire Go");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            name = extras.getString("Name");
//            email = extras.getString("Email");
//            //The key argument here must match that used in the other activity
//        }else {
//            Toast.makeText(getApplicationContext(), "NULL",Toast.LENGTH_SHORT).show();
//        }

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        email = intent.getStringExtra("Email");

        navigationView = findViewById(R.id.navigationbar);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView Uname = header.findViewById(R.id.NHUserName);
        TextView Uemail = header.findViewById(R.id.NHUserEmail);
        drawerLayout = findViewById(R.id.drawer_layout);
        ViewCompat.setNestedScrollingEnabled(navigationView.getChildAt(0), false);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        if (name != null && !name.isEmpty()){
            Uname.setText("Name:" + name);
        }else {
            Uname.setText("Chandan");
        }
        if (email != null && !email.isEmpty()){
            Uemail.setText("Email:" + email);
        }else {
            Uemail.setText("SIN:" + "XXXXXX101");
        }

        hideansShowFBLogout();
        hideansShowGoogleLogout();

        FBLogout = navigationView.findViewById(R.id.FBlogout);
        GLogout = navigationView.findViewById(R.id.Glogout);
        SignIn = navigationView.findViewById(R.id.signin);

        FBLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent FBLogout = new Intent(MasterActivity.this, LoginRegister.class);
                startActivity(FBLogout);
                Toast.makeText(getApplicationContext(), "LoggedOut from Facebook Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        GLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent GLogout = new Intent(MasterActivity.this, LoginRegister.class);
                        startActivity(GLogout);
                        Toast.makeText(getApplicationContext(), "LoggedOut from Google Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterActivity.this, LoginRegister.class);
                startActivity(intent);
            }
        });

        bottomNavigation = findViewById(R.id.bottombar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

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
//        getMenuInflater().inflate(R.menu.toolbar_items, menu);
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

            case R.id.nWordPower:
                loadWordPower();
                break;

            case R.id.nToDo:
                loadToDoReminder();
                break;

//            case R.id.nPoll:
//                Toast.makeText(this, "Poll is coming soon", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nMchallenge:
//                Toast.makeText(this, "Monthly Challege is coming soon", Toast.LENGTH_SHORT).show();
//                break;

            case R.id.nTofw:
                Toast.makeText(this, "Topic of the Week is coming soon", Toast.LENGTH_SHORT).show();
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

//            case R.id.logout:
//                AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                Menu nav_Menu = navigationView.getMenu();
//                if (isSignedIn()){
//                    nav_Menu.findItem(R.id.logout).setTitle("G-Logout");
//                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                            Intent GLogout = new Intent(MasterActivity.this, LoginRegister.class);
//                            startActivity(GLogout);
//                            Toast.makeText(getApplicationContext(), "LoggedOut from Google Successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }else if (accessToken == null) {
//                    nav_Menu.findItem(R.id.logout).setTitle("FB-Logout");
//                    LoginManager.getInstance().logOut();
//                    Intent FbLogout = new Intent(MasterActivity.this, LoginRegister.class);
//                    startActivity(FbLogout);
//                    Toast.makeText(getApplicationContext(), "LoggedOut from Facebook Successfully", Toast.LENGTH_SHORT).show();
//                }else {
//                    nav_Menu.findItem(R.id.logout).setTitle("SignIn");
//                    Intent intent = new Intent(MasterActivity.this, LoginRegister.class);
//                    startActivity(intent);
//                }
//                break;

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
//
//            case R.id.naboutApp:
//                Intent aboutapp = new Intent(MasterActivity.this, aboutactivity.class);
////                aboutactivity.reachUsat.setVisibility(View.INVISIBLE);
////                aboutactivity.aboutUs.setVisibility(View.VISIBLE);
////                aboutactivity.contactUs.setVisibility(View.VISIBLE);
//                startActivity(aboutapp);
//                break;
            case R.id.naboutUs:
                Intent aboutus = new Intent(MasterActivity.this, aboutactivity.class);
//                aboutactivity.reachUsat.setVisibility(View.INVISIBLE);
//                aboutactivity.aboutUs.setVisibility(View.VISIBLE);
//                aboutactivity.contactUs.setVisibility(View.VISIBLE);
                startActivity(aboutus);
                break;
//            case R.id.nReachus:
//                Intent reachus = new Intent(MasterActivity.this, aboutactivity.class);
////                aboutactivity.reachUsat.setVisibility(View.VISIBLE);
////                aboutactivity.aboutUs.setVisibility(View.INVISIBLE);
////                aboutactivity.contactUs.setVisibility(View.VISIBLE);
//                startActivity(reachus);
//                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("CatFrag");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment != null && currentFragment.isVisible()){
            finish();
        }else {
            loadCategorys();
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
                    if (toolbar.isShown() && bottomNavigation.isShown() && appBarLayout.isShown() && fab.isShown()){
//                    if (getSupportActionBar().isShowing() && bottomNavigation.isShown()) {
//                        getSupportActionBar().hide();
                        toolbar.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.INVISIBLE);
                        bottomNavigation.setVisibility(View.INVISIBLE);
                        appBarLayout.setVisibility(View.INVISIBLE);
                    } else {
                        toolbar.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
//                        getSupportActionBar().show();
                        bottomNavigation.setVisibility(View.VISIBLE);
                        appBarLayout.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else return false;
            }
        });
    }

    public void loadCategorys() {
        Intent intent = getIntent();
        String notificationType = intent.getStringExtra("NextFragment");
        if(notificationType!=null){
            intent.removeExtra("NextFragment");

            if(notificationType.equalsIgnoreCase("SPDM")){
               loadSpeedMaths();

            }
            if(notificationType.equalsIgnoreCase("GK")){
             loadGK();

            } if(notificationType.equalsIgnoreCase("PUZ")){
               loadPuzzle();

            } if(notificationType.equalsIgnoreCase("WORD")){
                loadWordPower();

            }if(notificationType.equalsIgnoreCase("MOT")){
                loadMotivation();

            }

        }else{
            FragmentManager fmhome = getSupportFragmentManager();
            CategoriesFragment Catefragment = new CategoriesFragment();
            fmhome.beginTransaction().add(R.id.mainFragment, Catefragment, "CatFrag").commit();
            FragmentTransaction Ctransaction = getSupportFragmentManager().beginTransaction();
            Ctransaction.replace(R.id.mainFragment, Catefragment, "CatFrag").addToBackStack(null).commit();
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Winspire Go");
    }

    public void loadWordPower() {
        FragmentManager fmhome = getSupportFragmentManager();
        WordPowerFragment WPfragment = new WordPowerFragment();
        fmhome.beginTransaction().add(R.id.mainFragment, WPfragment, "WordPowerFrag").commit();
        FragmentTransaction WPtransaction = getSupportFragmentManager().beginTransaction();
        WPtransaction.replace(R.id.mainFragment, WPfragment,"WordPowerFrag").addToBackStack(WPfragment.getClass().getName()).commit();
        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Word Power");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadToDoReminder() {
        FragmentManager fmhome = getSupportFragmentManager();
        ReminderFragment ToDofragment = new ReminderFragment();
        fmhome.beginTransaction().add(R.id.mainFragment, ToDofragment, "ToDoFrag").commit();
        FragmentTransaction ToDotransaction = getSupportFragmentManager().beginTransaction();
        ToDotransaction.replace(R.id.mainFragment, ToDofragment, "ToDoFrag").addToBackStack(ToDofragment.getClass().getName()).commit();
        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("ToDo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadMotivation() {
        FragmentManager fmMoti = getSupportFragmentManager();
        MotivationFragment Motifragment = new MotivationFragment();
        fmMoti.beginTransaction().add(R.id.mainFragment, Motifragment, "MotiFrag").commit();
        FragmentTransaction Mtransaction = getSupportFragmentManager().beginTransaction();
        Mtransaction.replace(R.id.mainFragment, Motifragment, "MotiFrag").addToBackStack(Motifragment.getClass().getName()).commit();
        shareit.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Motivation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadPuzzle() {
        FragmentManager fmPuzzle = getSupportFragmentManager();
        PuzzleFragment puzzlefragment = new PuzzleFragment();
        fmPuzzle.beginTransaction().add(R.id.mainFragment, puzzlefragment, "PuzzleFrag").commit();
        FragmentTransaction Ptransaction = getSupportFragmentManager().beginTransaction();
        Ptransaction.replace(R.id.mainFragment, puzzlefragment, "PuzzleFrag").addToBackStack(puzzlefragment.getClass().getName()).commit();
        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Puzzle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadGK() {
        FragmentManager fmGK = getSupportFragmentManager();
        GKFragment GKfragment = new GKFragment();
        fmGK.beginTransaction().add(R.id.mainFragment, GKfragment,"GKFrag").commit();
        FragmentTransaction GKtransaction = getSupportFragmentManager().beginTransaction();
        GKtransaction.replace(R.id.mainFragment, GKfragment, "GKFrag").addToBackStack(GKfragment.getClass().getName()).commit();
        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("General Knowledge");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadSpeedMaths() {
        FragmentManager fmspdm = getSupportFragmentManager();
        SpeedMathsFragment spdmfragment = new SpeedMathsFragment();
        fmspdm.beginTransaction().add(R.id.mainFragment, spdmfragment, "SPDMFrag").commit();
        FragmentTransaction spdmtransaction = getSupportFragmentManager().beginTransaction();
        spdmtransaction.replace(R.id.mainFragment, spdmfragment, "SPDMFrag").addToBackStack(spdmfragment.getClass().getName()).commit();
        shareit.setVisibility(View.INVISIBLE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Speed Maths");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    private void hideansShowFBLogout() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            navigationView = findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.fbLogout).setVisible(false);
//            FBLogout.setVisibility(View.GONE);
        } else {
            navigationView = findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.fbLogout).setVisible(true);
//            FBLogout.setVisibility(View.VISIBLE);
        }
    }
    private void hideansShowGoogleLogout() {
        if (isSignedIn()) {
            navigationView = findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.gLogout).setVisible(true);
//            GLogout.setVisibility(View.VISIBLE);
        } else {
            navigationView = findViewById(R.id.navigationbar);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.gLogout).setVisible(false);
//            GLogout.setVisibility(View.GONE);
        }
    }
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }


}
