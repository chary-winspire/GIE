package com.example.extarc.androidpushnotification;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_PICTURES;
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
public class PuzzleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {

    private final String TAG = "PuzzleFragment";
    private String questionnaireListStr = null;

    TextView question, explanation;
    int count = 0;
    Button startpuzzle, submitpuzzle, DatePicBtn;
    RadioGroup rgrouppuzzle;
    RadioButton radioButton;

    RelativeLayout ansLayPuzzle;
    ImageButton sharePuzzle;
    private int STORAGE_PERMISSION_CODE = 23;
    private File imagePath;

    RelativeLayout startpuzzleLayout, layoutpuzzle;
    RadioButton option1, option2, option3, option4;
    Animation animation;

    public PuzzleFragment() {
        // Required empty public constructor
    }

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();

    Calendar calender;
    DatePicker datePicker;
    int selected_year, selected_month, selected_day;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat;
    String selectedDate;
    int ID=1;
    public SharedPreferences preferences;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.puzzle_view, container, false);

        long date = System.currentTimeMillis();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String dateStr = simpleDateFormat.format(date);
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID= Integer.valueOf(preferences.getString(Constants.PUZZLE,"1"));
        calender = Calendar.getInstance();
        selected_year = calender.get(Calendar.YEAR);
        selected_month = calender.get(Calendar.MONTH);
        selected_day = calender.get(Calendar.DAY_OF_MONTH);
        DatePicBtn = myview.findViewById(R.id.datePickerPuzzle);
        DatePicBtn.setText(dateStr);
        datePickerDialog = new DatePickerDialog(getContext(), this, selected_year, selected_month, selected_day);
        final long today = System.currentTimeMillis();
        final long oneDay = 24 * 60 * 60 * 1000L;
        DatePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(today - 7 * oneDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });
        selectedDate = DatePicBtn.getText().toString();
        Log.d(TAG, "selectedDate" + selectedDate);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(layoutParams);
        toolbartitle.setVisibility(View.GONE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("Brain Teasers");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));
        toolbar.setNavigationIcon(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        getQuestionnaire(ID, "PUZ");

        question = myview.findViewById(R.id.questionPuzzle);
        explanation = myview.findViewById(R.id.explanationPuzzle);
        startpuzzle = myview.findViewById(R.id.startPuzzle);
        submitpuzzle = myview.findViewById(R.id.submitPuzzle);

        ansLayPuzzle = myview.findViewById(R.id.ansLayout);
        ansLayPuzzle.setVisibility(View.INVISIBLE);
        sharePuzzle = myview.findViewById(R.id.sharePuzzleReview);
        sharePuzzle.setVisibility(View.INVISIBLE);

        question = myview.findViewById(R.id.questionPuzzle);
        explanation = myview.findViewById(R.id.explanationPuzzle);
        startpuzzle = myview.findViewById(R.id.startPuzzle);
        submitpuzzle = myview.findViewById(R.id.submitPuzzle);
        rgrouppuzzle = myview.findViewById(R.id.rgroupPuzzle);
        rgrouppuzzle.setOnCheckedChangeListener(this);

        option1 = myview.findViewById(R.id.puzzleOp1);
        option2 = myview.findViewById(R.id.puzzleOp2);
        option3 = myview.findViewById(R.id.puzzleOp3);
        option4 = myview.findViewById(R.id.puzzleOp4);

        startpuzzleLayout = myview.findViewById(R.id.layoutBtnStartPuzzle);
        layoutpuzzle = myview.findViewById(R.id.layoutPuzzle);

        sharePuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStorageReadable()) {
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    shareIt();
                    return;
                }
                requestStoragePermission();
            }
        });

        startpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        submitpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                if (rgrouppuzzle.getCheckedRadioButtonId() == -1) {
                    // no radio buttons are checked
                    Toast.makeText(getContext(), "Please Select Answer", Toast.LENGTH_SHORT).show();
                    submitpuzzle.setVisibility(View.VISIBLE);
                } else {
                    // one of the radio buttons is checked
                    int selectedId = rgrouppuzzle.getCheckedRadioButtonId();  // get selected radio button from radioGroup
                    radioButton = (RadioButton) myview.findViewById(selectedId);  // find the radiobutton by returned id
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Are Sure you want to submit");
                    builder.setMessage("Your selected answer is: " + radioButton.getText().toString());

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getContext(), radioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < rgrouppuzzle.getChildCount(); i++) {
                                rgrouppuzzle.getChildAt(i).setEnabled(false);
                            }

                            TextView question, correctAns, explanationR;
                            ImageView ansImage;
                            question =myview.findViewById(R.id.questionReview);
                            correctAns = myview.findViewById(R.id.AnsPuzzle);
                            ansImage =myview.findViewById(R.id.ansImage);
                            explanationR = myview.findViewById(R.id.explanationReview);

                            String userAns = radioButton.getText().toString();
                            question.setText(arryJobDetails.get(count).getQuestion());
                            correctAns.setText(arryJobDetails.get(count).getAnswer());
                            explanationR.setText(arryJobDetails.get(count).getComment());

                            submitpuzzle.setVisibility(View.INVISIBLE);
                            rgrouppuzzle.setVisibility(View.INVISIBLE);
                            sharePuzzle.setVisibility(View.VISIBLE);
                            ansLayPuzzle.setVisibility(View.VISIBLE);
                            layoutpuzzle.setVisibility(View.INVISIBLE);

                            if ((correctAns.getText().toString().equalsIgnoreCase(userAns))) {
                                correctAns.setTextColor(Color.parseColor("#40fd1a"));
                                ansImage.setBackgroundResource(R.drawable.ic_right);
                            } else {
                                correctAns.setTextColor(Color.parseColor("#40fd1a"));
                                ansImage.setBackgroundResource(R.drawable.ic_wrong);
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            submitpuzzle.setVisibility(View.VISIBLE);
                            layoutpuzzle.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.show();
                }

            }
        });
        return myview;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selected_year = year;
        selected_month = month;
        selected_day = dayOfMonth;
        updateDate();
        Toast.makeText(getActivity(), "Selected Date: " + view.getYear() + "/" + (view.getMonth() + 1) + "/" + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
    }
    private void updateDate() {
        DatePicBtn.setText(new StringBuilder().append(selected_day).append("-").append(selected_month + 1).append("-").append(selected_year).append(" "));
    }

    //    public void getQuestionnaire(final int id, final String type) {
//        Log.i("getQuestionnaire", " *** getQuestionnaire started");
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                Log.i("getAllJobs", " *** getAllJobs doInBackground started");
//                String SERVICE_URI = ApplicationConstants.SERVER_PATH;
//                String strUrl = "/getQuestionnaire?id=" + id + "&type=" + type;
//                try {
//                    questionnaireListStr = WebService.callJsonService(strUrl, "getQuestionnaire", null, Constants.GET);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                Log.i("getAllJobs", " *** getAllJobs doInBackground completed");
//                return questionnaireListStr;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                try {
//                    Log.i("getAllJobs", " *** getAllJobs onPostExecute started");
//                    String jsonAllJobs = "";
//                    JSONObject jsonString = new JSONObject(msg);
//                    jsonAllJobs = jsonString.getString("questionnaireStr");
//                    Log.e(TAG, jsonAllJobs);
//                    BindJobsList(jsonAllJobs);
//
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                Log.i(TAG, "PreExecute Called");
//            }
//        }.execute(null, null, null);
//    }
    public void getQuestionnaire(final int id, final String type) {
        Log.i("getQuestionnaire", " *** getQuestionnaire started");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.i("getAllJobs", " *** getAllJobs doInBackground started");
                String SERVICE_URI = ApplicationConstants.SERVER_PATH;
                String strUrl = "/getQuestionnaire?id=" + id + "&type=" + type;
                try {
                    questionnaireListStr = WebService.callJsonService(strUrl, "getQuestionnaire", null, Constants.GET);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.i("getAllJobs", " *** getAllJobs doInBackground completed");
                return questionnaireListStr;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    Log.i("getAllJobs", " *** getAllJobs onPostExecute started");
                    String jsonAllJobs = "";
                    JSONObject jsonString = new JSONObject(msg);
                    jsonAllJobs = jsonString.getString("questionnaireStr");
                    Log.e(TAG, jsonAllJobs);
                    BindJobsList(jsonAllJobs);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Log.i(TAG, "PreExecute Called");
            }
        }.execute(null, null, null);
    }

    private void BindJobsList(String jsonAllJobs) {
        Questionnaire objJobDetails = new Questionnaire();
        Gson gson = new Gson();
        try {
            if (jsonAllJobs != "" && jsonAllJobs != "null" && jsonAllJobs != null) {
                JSONArray jsonPArray = new JSONArray(jsonAllJobs);
                Log.i(TAG, jsonAllJobs);
                if (jsonPArray.length() > 0) {
                    for (int i = 0; i < jsonPArray.length(); i++) {
                        objJobDetails = new Questionnaire();
                        String jsonJob = jsonPArray.getJSONObject(i).toString();
                        objJobDetails = gson.fromJson(jsonJob, Questionnaire.class);
                        arryJobDetails.add(objJobDetails);
                    }

                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());
                    explanation.setText(arryJobDetails.get(count).getComment());
                    Animation();

//                       lviewAdapter = new CustomSpdMAdapter(getActivity(), arryJobDetails);
//                        JobsListView.setAdapter(lviewAdapter);


                }
            } else {
                Log.i(TAG, " List completed");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rgroupPuzzle:
                if (option1.isChecked()) {
                    option1.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                } else if (option2.isChecked()) {
                    option2.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option1.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                } else if (option3.isChecked()) {
                    option3.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option1.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                } else if (option4.isChecked()) {
                    option4.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                    option1.setBackgroundColor(getResources().getColor(R.color.Puzzle_Toolbar));
                }
                break;
        }
    }

    public void Animation() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.fall_down);
        option1.startAnimation(animation);
        option2.startAnimation(animation);
        option3.startAnimation(animation);
        option4.startAnimation(animation);
    }

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
                Toast.makeText(getApplicationContext(), "Oops you just denied the Storage permission", Toast.LENGTH_LONG).show();
            }
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
        Toast.makeText(getApplicationContext(), "Image Saved at: " + imagePath, Toast.LENGTH_SHORT).show();
    }

    public Bitmap takeScreenshot() {
        ansLayPuzzle.setDrawingCacheEnabled(true);
        return ansLayPuzzle.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
