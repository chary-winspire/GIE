package com.example.extarc.androidpushnotification;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.example.extarc.androidpushnotification.Services.DownloadImageTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.directory;
import static com.example.extarc.androidpushnotification.MasterActivity.directory_Moti;
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
    String TAG = "MotivationFragment";
    ImageView motiimage;
    RelativeLayout ivMotiLayout;
    ImageButton nextButton;
    ImageButton previousButton;
    LinearLayout layout1, layout2;
    RelativeLayout motivationLayout;
    int counter = 0;
    List<Questionnaire> notiList = null;
    int ID = 0;
    public SharedPreferences preferences;
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;

    private int STORAGE_PERMISSION_CODE = 23;
    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
    private String imageName = "Motivation";
    private File imagePath = new File(directory_Moti + "/" + imageName + timeStamp + ".jpeg");
    public static ImageButton shareit;

    public MotivationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivation, container, false);

        ivMotiLayout = view.findViewById(R.id.ivMotivationLayout);
        //ivMotiLayout.setVisibility(View.GONE);

        shareit = view.findViewById(R.id.shareMoti);
        shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMotiLayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //First checking if the app is already having the permission
                        if (isStorageReadable()) {
                            //If permission is already having then showing the toast
                            Bitmap bitmap = takeScreenshot();
                            saveBitmap(bitmap, imageName);
                            shareIt();
                            //Existing the method with return
                        }else {
                            //If the app has not the permission then asking for the permission
                            requestStoragePermission();
                        }
                    }
                }, 200);

            }
        });

//        /* adapt the image to the size of the display */
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//                getResources(), R.drawable.image), size.x, size.y, true);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        appBarLayout.setLayoutParams(layoutParams);
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
//       motiimage.setImageBitmap(bmp);
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID = Integer.valueOf(preferences.getString(Constants.MOTIVATION, "0"));
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
            motivationLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.isConnected() && netInfo.isAvailable()) {
                getNotificationDetails(ID, "MOT");
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
                String url = "/getQuestionnaire?id=" + id + "&type=" + type;
                ;
                ;
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
            Log.d(TAG, "Image url Called" + url);
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

    //We are calling this method to check the permission status
    private boolean isStorageReadable() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Permission Required to Access Storage", Toast.LENGTH_SHORT).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the Storage permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveBitmap(Bitmap bitmap, String imageName) {
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
        Toast.makeText(getActivity(), "Image Saved at: " + directory_Moti, Toast.LENGTH_SHORT).show();
    }

    public Bitmap takeScreenshot() {
//        View rootView = findViewById(android.R.id.content).getRootView();
        ivMotiLayout.setDrawingCacheEnabled(true);
        return ivMotiLayout.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void addImageWaterMark() {
        Bitmap bitmap = null;

        try {
            File f = new File(directory);
            if (f.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try
                {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                    Bitmap output = imageWatermark.addWatermark(getResources(), bitmap);
                    /*save image to sdcard*/
                    saveBitmap(output, imageName);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
