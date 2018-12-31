package com.example.extarc.androidpushnotification;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.drawerLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.fab;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbar;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle2;


/**
 * A simple {@link Fragment} subclass.
 */
public class aboutFragment extends Fragment implements View.OnClickListener {

    TextView versionName;
    public static LinearLayout aboutUs, reachUsat, contactUs;
    ImageView companyLogo;
    Button rateUs;
    Button BTNcontactUS;
    Button appFacebook, appTwitter, appYoutube;
    Button abCompany, abTandC, abPPolicy;


    public aboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        appBarLayout.setLayoutParams(layoutParams);
//        toolbar.setTitle("Speed Maths");
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle2.setVisibility(View.INVISIBLE);
        toolbartitle.setText("Winspire Go");
        toolbartitle.setTextColor(getResources().getColor(R.color.White));
        toolbar.setBackgroundColor(getResources().getColor(R.color.QuoraRed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
//        toolbar.setNavigationIcon(null);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        aboutUs = view.findViewById(R.id.aboutUs);
        reachUsat = view.findViewById(R.id.reachUs);
        contactUs = view.findViewById(R.id.contact);
        companyLogo = view.findViewById(R.id.iv);
        rateUs = view.findViewById(R.id.rateUs);
        appFacebook = view.findViewById(R.id.abFacebook);
        appTwitter = view.findViewById(R.id.abTwitter);
        appYoutube = view.findViewById(R.id.abYoutube);
        abCompany = view.findViewById(R.id.abCompany);
        abTandC = view.findViewById(R.id.abTandC);
        abPPolicy = view.findViewById(R.id.abPPolicy);
        BTNcontactUS = view.findViewById(R.id.contactUs);

        String version = null;
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = view.findViewById(R.id.textview);
        versionName.setText("Version " + version);

        rateUs.setOnClickListener(this);
        appFacebook.setOnClickListener(this);
        appTwitter.setOnClickListener(this);
        appYoutube.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        abCompany.setOnClickListener(this);
        abPPolicy.setOnClickListener(this);
        abTandC.setOnClickListener(this);
        BTNcontactUS.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rateUs:
                rateMe(v);
                break;
            case R.id.abFacebook:
                toFacebook(v);
                break;
            case R.id.abTwitter:
                toTwitter(v);
                break;
            case R.id.abYoutube:
                toYoutube(v);
                break;
            case R.id.abCompany:

                break;
            case R.id.abPPolicy:

                break;
            case R.id.abTandC:

                break;
            case R.id.contactUs:

                break;
        }

    }

    public void rateMe(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getActivity().getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?di=" + getActivity().getPackageName())));
        }
    }

    public void toFacebook(View view) {
        String yourFBpageId = "https://www.facebook.com/win.spire.33";
        try {
            //try to open page in facebook native app.
            String uri = "fb://page/" + yourFBpageId;    //Cutsom URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //facebook native app isn't available, use browser.
            String uri = "http://touch.facebook.com/pages/x/" + yourFBpageId;  //Normal URL
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        }
    }

    public void toTwitter(View view) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
        }
        this.startActivity(intent);
    }

    public void toYoutube(View view) {
        String youtubelink = "https://www.youtube.com/";
        try {
            //try to open page in facebook native app.
            String uri = "fb://page/" + youtubelink;    //Cutsom URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubelink));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //facebook native app isn't available, use browser.
            String uri = "http://touch.facebook.com/pages/x/" + youtubelink;  //Normal URL
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubelink));
            startActivity(i);
        }
    }
}
