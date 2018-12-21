package com.example.extarc.androidpushnotification;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class aboutactivity extends AppCompatActivity implements View.OnClickListener {

    TextView versionName;
    public static LinearLayout aboutUs, reachUsat, contactUs;
    ImageView companyLogo;
    Button rateUs;
    Button BTNcontactUS;
    Button appFacebook, appTwitter, appYoutube;
    Button abCompany, abTandC, abPPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutUs = findViewById(R.id.aboutUs);
        reachUsat = findViewById(R.id.reachUs);
        contactUs = findViewById(R.id.contact);
        companyLogo = findViewById(R.id.iv);
        rateUs = findViewById(R.id.rateUs);
        appFacebook = findViewById(R.id.abFacebook);
        appTwitter = findViewById(R.id.abTwitter);
        appYoutube = findViewById(R.id.abYoutube);
        abCompany = findViewById(R.id.abCompany);
        abTandC = findViewById(R.id.abTandC);
        abPPolicy = findViewById(R.id.abPPolicy);
        BTNcontactUS = findViewById(R.id.contactUs);

        String version = null;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = findViewById(R.id.textview);
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
    }

    public void rateMe(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?di=" + this.getPackageName())));
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
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
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
}
