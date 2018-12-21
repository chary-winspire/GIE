package com.example.extarc.androidpushnotification;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.example.extarc.androidpushnotification.Services.DownloadImageTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.drawerLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.fab;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbar;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle2;
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
    RelativeLayout motivationLayout;
    int counter = 0;
    List<Questionnaire> notiList = null;
    int ID=0;
    public SharedPreferences preferences;
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;

    public MotivationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivation, container, false);

//        /* adapt the image to the size of the display */
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//                getResources(), R.drawable.image), size.x, size.y, true);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(layoutParams);
        toolbartitle.setVisibility(View.GONE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("Daily Kickstart");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));

        toolbar.setBackgroundColor(getResources().getColor(R.color.Black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        motiimage = view.findViewById(R.id.ivMotivation);
        /* fill the background ImageView with the resized image */
//        motiimage.setImageBitmap(bmp);
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID= Integer.valueOf(preferences.getString(Constants.MOTIVATION,"0"));
        nextButton = view.findViewById(R.id.nextMoti);
        previousButton = view.findViewById(R.id.previousMoti);
        layout1 = view.findViewById(R.id.previousLayout1);
        layout2 = view.findViewById(R.id.previousLayout2);
        motivationLayout = view.findViewById(R.id.motivationLayout);

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
        if (netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.isConnected() && netInfo.isAvailable()) {
//            Toast.makeText(getActivity(), "Connected to Internet", Toast.LENGTH_SHORT).show();
            return true;
        } else {
//            Toast.makeText(getActivity(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            ShowProgressDialog();
            motivationLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.isConnected() && netInfo.isAvailable()) {
                getNotificationDetails(ID,"MOT");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isOnline()) {
                alertDialog.dismiss();
                motivationLayout.setVisibility(View.VISIBLE);
            } else {
                setAlertDialog();
            }
        }
    }

    private void getNotificationDetails(final int id, final String type) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String url = "/getQuestionnaire?id=" + id + "&type=" + type;;;
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
                    String notiStr = resObj.getString("questionnaireStr");
                    JSONArray notiArray = new JSONArray(notiStr);

                    notiList = new ArrayList<>();
                    for (int i = 0; i < notiArray.length(); i++) {
                        Questionnaire notificationDetail = new Questionnaire();
                        notificationDetail = gson.fromJson(notiArray.getJSONObject(i).toString(), Questionnaire.class);
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


    private void bindImage(List<Questionnaire> notiList, ImageView imageView, int index) {
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
            String url = ApplicationConstants.SERVER_PATH + "getImage?imageName=" + notiList.get(index).getQuestion();
            Log.d(TAG, "Image url Called"+url);
            new DownloadImageTask(imageView).execute(url);
        }
    }

    public void ShowProgressDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progressbar, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void setAlertDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setTitle("No internet !");
        builder.setMessage("Please Try again after some time");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialog.dismiss();
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new CategoriesFragment()).commit();
                }
            }
        });
        builder.show();
    }
}
