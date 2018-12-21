package com.example.extarc.androidpushnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.UUID;

public class FirstActivity extends AppCompatActivity {
    String TAG = "FirstActivity";
    Context applicationContext;
    public SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

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
        loginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FirstActivity.this, LoginRegister.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FirstActivity.this, MasterActivity.class);
                startActivity(intent);
            }
        });
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoginRegister.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();

        Log.i(TAG, "SharedPreferences PersistentValues File commit: " + preferences.getString(Constants.JSON_USERDETAILS, ""));
        storeFCMToken( preferences.getString(Constants.FCM_ID, ""),preferences.getString(Constants.DEVICE_ID, ""));
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
}
