package com.example.extarc.androidpushnotification;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Models.NotificationDetails;
import com.example.extarc.androidpushnotification.Services.DownloadImageTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MotivationFragment extends Fragment {

    //    private static ArrayList<MotivationModel> data;
    String TAG = "Motivation Fragment";
    ImageView motiimage;
    ImageButton nextButton;
    ImageButton previousButton;
    LinearLayout layout1, layout2;
    int counter = 0;
    List<NotificationDetails> notiList = null;

    private ProgressBar progressBar;
    LinearLayout loadingMoti;

    public MotivationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivation, container, false);

        motiimage = view.findViewById(R.id.ivMotivation);

        nextButton = view.findViewById(R.id.nextMoti);
        previousButton = view.findViewById(R.id.previousMoti);
        layout1 = view.findViewById(R.id.previousLayout1);
        layout2 = view.findViewById(R.id.previousLayout2);

        progressBar = view.findViewById(R.id.progressbarMoti);
        loadingMoti = view.findViewById(R.id.loadingMoti);
        new ProgressTask().execute();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                bindImage(notiList, motiimage, counter);

            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter - 1;
                bindImage(notiList, motiimage, counter);

            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter - 1;
                bindImage(notiList, motiimage, counter);

            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                bindImage(notiList, motiimage, counter);

            }
        });

        return view;

//        new DownloadImageTask((ImageView) view.findViewById(R.id.Motivation_image))
//                .execute(MY_URL_STRING);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingMoti.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (isOnline()) {
                getNotificationDetails();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getApplicationContext());
                        alertdialog.setCancelable(false);
                        alertdialog.setTitle("Error...");
                        alertdialog.setMessage("No Internet Connection Found.");
                        alertdialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isOnline();
                            }
                        });
                        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertdialog.show();
                    }
                });
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loadingMoti.setVisibility(View.GONE);
        }
    }

    private void getNotificationDetails() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String url = "getNotificationDetails";
                String JsonResponse = null;
                try {

                    String SERVICE_URI = ApplicationConstants.SERVER_PATH;
                    JSONStringer JsonInputStringer = new JSONStringer();

                    Log.d(TAG, "JsonInputStringer " + JsonInputStringer);
                    JsonResponse = WebService.callJsonService(url, TAG + ".saveLeadInfoDetails", JsonInputStringer, Constants.GET);
                    return JsonResponse;
                } catch (Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage(), ex);
                }
                return JsonResponse;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    Gson gson = new Gson();
                    JSONObject resObj = new JSONObject(msg);
                    String notiStr = resObj.getString("notificationDetails");
                    JSONArray notiArray = new JSONArray(notiStr);

                    notiList = new ArrayList<>();
                    for (int i = 0; i < notiArray.length(); i++) {
                        NotificationDetails notificationDetail = new NotificationDetails();
                        notificationDetail = gson.fromJson(notiArray.getJSONObject(i).toString(), NotificationDetails.class);
                        notiList.add(notificationDetail);
                    }
                    bindImage(notiList, motiimage, counter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private void bindImage(List<NotificationDetails> notiList, ImageView imageView, int index) {
        if (index == 0) {
            previousButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
        if (index > 0) {
            previousButton.setVisibility(View.VISIBLE);
        }
        if (index == notiList.size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.VISIBLE);
        }
        if (index < notiList.size() - 1) {
            nextButton.setVisibility(View.VISIBLE);
        }
        if (index >= 0 && index < notiList.size()) {
            String url = ApplicationConstants.SERVER_PATH + "getImage?imageName=" + notiList.get(index).getNotificationImage();
            new DownloadImageTask(imageView).execute(url);
        }

    }

}
