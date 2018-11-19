package com.example.extarc.androidpushnotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

public class MainActivity extends AppCompatActivity {

    private Button subscribe;
    private Button unsubscribe;
    Context applicationContext;
    String TAG = "MainActivity";
    private final String TOPIC = "JavaSampleApproach";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscribe = (Button) findViewById(R.id.subscribe);
        unsubscribe = (Button) findViewById(R.id.unsubscribe);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
            }
        });

        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC);

            }
        });
        String token = FirebaseInstanceId.getInstance().getToken();
        //storeFCMToken(token);


    }

    private void storeFCMToken(final String FCMToken) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String JsonResponse = null;
                String url = "/json/insertGCMID";
                Log.i(TAG, "Starting storeGCMIdInServer");
                Gson gson1 = new Gson();
                String JsonUserDetails = gson1.toJson(FCMToken);
                Log.i("UserDetails", JsonUserDetails);
                try {
                    // Build JSON string
                    JSONStringer JsonInputStringer = new JSONStringer()
                            .object()
                            .key("responseText")
                            .object()
                            .key("details").value(JsonUserDetails)
                            .endObject()
                            .endObject();
                    Log.i(TAG, "Input to WCF in storeGCMIdInServer: " + JsonInputStringer.toString());
                    JsonResponse = WebService.callJsonService(url, TAG + ".storeGCMIdInServer", JsonInputStringer, Constants.POST);
                    return JsonResponse;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e(TAG, ex.getMessage(), ex);

                }
                return JsonResponse;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    SharedPreferences persistentValues = getSharedPreferences(Constants.SP_PERSISTENT_VALUES, Context.MODE_PRIVATE);
                    Log.i(TAG, "Response from server in storeGCMIdInServer: " + msg);
                    if (msg != null) {
                        JSONObject jsoMain = new JSONObject(msg);
                        if (jsoMain.getBoolean("insertGCMIDResult") == true) {
                            Log.i(TAG, "GCM details inserted  successfully.");
                            PackageManager manager = applicationContext.getPackageManager();
                            PackageInfo info = manager.getPackageInfo(
                                    applicationContext.getPackageName(), 0);
                            String version = info.versionName;
                            SharedPreferences.Editor editor = persistentValues.edit();
                            // Log.d(TAG, "Putting following GCM into SP: " + userDetails.getGCMId() + version);
                            //editor.putString(Constants.GCM_ID, userDetails.getGCMId());
                            editor.putString(Constants.APPLICATION_VERSION_NAME, version);

                            // editor.putString(Constants.DEVICE_ID, userDetails.getAndroidDeviceId());

                            editor.commit();
                            Log.i(TAG, "SharedPreferences PersistentValues File commit: " + persistentValues.getAll().toString());
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

