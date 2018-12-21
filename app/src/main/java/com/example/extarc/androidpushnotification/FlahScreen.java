package com.example.extarc.androidpushnotification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.util.Util;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

public class FlahScreen extends AppCompatActivity {

    final int SPLASH_DISPLAY_LENGTH = 3000;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flah_screen);

        final ImageView logo = findViewById(R.id.logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        logo.startAnimation(animation);



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(FlahScreen.this, FirstActivity.class);
//                FlahScreen.this.startActivity(mainIntent);
//                FlahScreen.this.finish();

                Intent activityIntent;

                // go straight to main if a token is stored
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null || isSignedIn()) {
                    activityIntent = new Intent(FlahScreen.this, MasterActivity.class);
                } else {
                    activityIntent = new Intent(FlahScreen.this, LoginRegister.class);
                }
                startActivity(activityIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
}

