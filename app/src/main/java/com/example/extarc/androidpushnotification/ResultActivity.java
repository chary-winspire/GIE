package com.example.extarc.androidpushnotification;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle2;

public class ResultActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "ResultActivity";

    TextView correctAns;
    TextView incorrectAns;
    TextView attemptedQue;
    TextView Totalscore;
    TextView NotAttempted;
    TextView TimeLeft;
    int correct = 0, incorrect = 0, attempted = 0, score = 0, notAttempted = 0, your = 0;
    long timeleftMilli;
    long timeleftSec;
    LinearLayout attemptedLayout, correctLayout, incorrectLayout, notattempLayout, timeleftLayout;
    Button tryagain;
    TextView resultText, resultText1;

    String Question, Answer, UserAns, Comment;
    Menu nav_Menu;
    List<SliceValue> pieData = new ArrayList<>();
    String arraylistAns;
    JSONArray jsonArray;
    JSONObject jsonObject;
    private int STORAGE_PERMISSION_CODE = 23;
    private File imagePath;

    Animation leftTorightAnim, righttoleftAnim, blinkAnim;

    Button TitleResult;
    RelativeLayout layoutscore;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    View divider6;

    Button barButton;
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        barButton = findViewById(R.id.barButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getWindow());
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.round);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        tv = (TextView) findViewById(R.id.tv);

//        mProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.Black), PorterDuff.Mode.SRC_IN);
//        mProgress.getProgressDrawable().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_IN);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (pStatus < 100) {
//                    pStatus += 1;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgress.setProgress(pStatus);
//                            tv.setText(pStatus + "%");
////                            if (jsonArray.length() >= 6) {
////                                your = score * 10;
////                                mProgress.setProgress(your);
////                                tv.setText(your + "%");
////                            }else {
////                                your = score * 20;
////                                mProgress.setProgress(your);
////                                tv.setText(your + "%");
////                            }
//                        }
//                    });
//                }
//            }
//        }).start();

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        incorrect = intent.getIntExtra("wrong", 0);
        notAttempted = intent.getIntExtra("notattempted", 0);
        arraylistAns = intent.getStringExtra("userArray");
        timeleftMilli = intent.getLongExtra("timeleftMilli", 0);
        Log.d(TAG, "arraylistAns" + arraylistAns);

        timeleftSec = TimeUnit.MILLISECONDS.toSeconds(timeleftMilli);

        correctAns = (TextView) findViewById(R.id.correct);
        incorrectAns = (TextView) findViewById(R.id.incorrect);
        attemptedQue = (TextView) findViewById(R.id.attempted);
        Totalscore = (TextView) findViewById(R.id.score);
        NotAttempted = (TextView) findViewById(R.id.notAttempted);
        tryagain = (Button) findViewById(R.id.tryagainResult);
        resultText = (TextView) findViewById(R.id.resultText);
        resultText1 = (TextView) findViewById(R.id.resultText1);
        TimeLeft = (TextView) findViewById(R.id.timeLeft);

        attemptedLayout = (LinearLayout) findViewById(R.id.attemptedlayout);
        correctLayout = (LinearLayout) findViewById(R.id.correctlayout);
        incorrectLayout = (LinearLayout) findViewById(R.id.incorrectlayout);
        notattempLayout = (LinearLayout) findViewById(R.id.notattemptedlayout);
        timeleftLayout = (LinearLayout) findViewById(R.id.timeLeftlayout);

        bottomNavigationView = findViewById(R.id.BottombarSecond);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        layoutscore = findViewById(R.id.layoutscore);
        TitleResult = findViewById(R.id.titleresult);
        divider6 = findViewById(R.id.divider6);

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ResultActivity.this, MasterActivity.class);
                startActivity(intent1);
            }
        });

        PieChartView pieChartView = findViewById(R.id.chart);
        pieData.add(new SliceValue(10, getResources().getColor(R.color.ColorLightGrey)));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);

        incorrectAns.setText("  " + incorrect);
        correctAns.setText("  " + score);
        attempted = score + incorrect;

        int colorFrom = getResources().getColor(R.color.ColorLightGrey);
        int colorTo = getResources().getColor(R.color.Black);

        try {
            jsonArray = new JSONArray(arraylistAns);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray.length() >= 6) {
            your = score * 10;
            mProgress.setProgress(your);
            tv.setText(your + "%");
            Totalscore.setText("Total Score  :    " + your);
            attemptedQue.setText("  " + attempted + " / " + "10");
            NotAttempted.setText("  " + notAttempted + " / " + "10");
            TimeLeft.setText(" " + timeleftSec + " / " + "120 Seconds");
            pieData.add(new SliceValue(notAttempted, Color.BLUE).setLabel("Not Attempted"));
            pieData.add(new SliceValue(score, Color.GREEN).setLabel("Correct Answers"));
            pieData.add(new SliceValue(incorrect, Color.RED).setLabel("Incorrect Answers"));

            TitleResult.setBackgroundResource(R.color.SPDM_Toolbar);
            bottomNavigationView.setBackgroundResource(R.color.SPDM_dark);
            layoutscore.setBackgroundResource(R.color.SPDM_Toolbar);
            barButton.setBackgroundResource(R.color.SPDM_dark);
            barButton.setTextColor(getResources().getColor(R.color.White));

            TitleResult.setTextColor(getResources().getColor(R.color.Black));
            resultText.setTextColor(getResources().getColor(R.color.Black));
            resultText1.setTextColor(getResources().getColor(R.color.Black));
            attemptedQue.setTextColor(getResources().getColor(R.color.Black));
            divider6.setBackgroundResource(R.color.Black);
        } else {
            your = score * 20;
            mProgress.setProgress(your);
            tv.setText(your + "%");

            Totalscore.setText("Total Score: " + your);
            attemptedQue.setText("  " + attempted + " / " + "5");
            NotAttempted.setText("  " + notAttempted + " / " + "5");
            pieData.add(new SliceValue(notAttempted * 2, Color.BLUE).setLabel("Not Attempted"));
            pieData.add(new SliceValue(score * 2, Color.GREEN).setLabel("Correct Answers"));
            pieData.add(new SliceValue(incorrect * 2, Color.RED).setLabel("Incorrect Answers"));

            TitleResult.setBackgroundResource(R.color.GK);
            bottomNavigationView.setBackgroundResource(R.color.GK_dark);
            layoutscore.setBackgroundResource(R.color.GK);
            barButton.setBackgroundResource(R.color.GK_dark);
            barButton.setTextColor(getResources().getColor(R.color.White));

            TitleResult.setTextColor(getResources().getColor(R.color.White));
            resultText.setTextColor(getResources().getColor(R.color.White));
            resultText1.setTextColor(getResources().getColor(R.color.White));
            attemptedQue.setTextColor(getResources().getColor(R.color.White));
            divider6.setBackgroundResource(R.color.White);

            Totalscore.setTextColor(getResources().getColor(R.color.White));
            attemptedQue.setTextColor(getResources().getColor(R.color.White));
            correctAns.setTextColor(getResources().getColor(R.color.White));
            incorrectAns.setTextColor(getResources().getColor(R.color.White));
            NotAttempted.setTextColor(getResources().getColor(R.color.White));
        }

        if (your == 100) {
            resultText.setText("Your Score 100%");
        } else if (your >= 80) {
            resultText.setText("Your Score 80 to 90");
        } else if (your >= 60) {
            resultText.setText("Your Score 60 to 70");
        } else if (your >= 40) {
            resultText.setText("Your Score 40 to 50");
        } else if (your >= 20) {
            resultText.setText("Your Score 20 to 30");
        } else resultText.setText("Your Score below 20");

        leftTorightAnim = AnimationUtils.loadAnimation(this, R.anim.lefttorightin);
        righttoleftAnim = AnimationUtils.loadAnimation(this, R.anim.righttoleftin);
        blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink);
        blinkAnim.setRepeatMode(Animation.REVERSE);
        blinkAnim.setRepeatCount(Animation.INFINITE);
        attemptedLayout.startAnimation(leftTorightAnim);
        correctLayout.startAnimation(righttoleftAnim);
        incorrectLayout.startAnimation(leftTorightAnim);
        notattempLayout.startAnimation(righttoleftAnim);
        resultText.startAnimation(blinkAnim);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(3000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
//                textView.setBackgroundColor((int) animator.getAnimatedValue());
//                attemptedLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();


        ColorDrawable[] color = {new ColorDrawable(Color.RED), new ColorDrawable(Color.GREEN)};
        TransitionDrawable trans = new TransitionDrawable(color);
//        tv.setBackground(trans);
//        attemptedLayout.setBackground(trans);
        trans.startTransition(3000); // duration 3 seconds

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottombarSecond);
        bottomNavigationView.findViewById(R.id.b1Share).setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.b1Review:

                ListView listView = findViewById(R.id.listviewResult);
                CustomAdapterSPDM customAdapter = new CustomAdapterSPDM(jsonArray);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();

                if (jsonArray.length() >= 6){
                    listView.setBackgroundResource(R.color.PageBackground);
                }else {
                    listView.setBackgroundResource(R.color.PageBackground);
                }

                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                if (layoutscore.isShown()) {
                    listView.startAnimation(slideUp);
                    listView.setVisibility(View.VISIBLE);
                    layoutscore.setVisibility(View.INVISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.b1Review).setTitle("Score Card");
                    barButton.setVisibility(View.VISIBLE);
                    barButton.setText("Test Review");
                } else {
                    listView.startAnimation(slideDown);
                    listView.setVisibility(View.INVISIBLE);
                    layoutscore.setVisibility(View.VISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.b1Review).setTitle("Review");
                    barButton.setVisibility(View.GONE);
                }
                break;
        }
        return false;
    }

    class CustomAdapterSPDM extends BaseAdapter {

        JSONArray jsonArray;
        Context context;

        public CustomAdapterSPDM(JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @Override
        public Object getItem(int position) {
            try {
                return jsonArray.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            private ImageView resultView;
            private TextView question;
            private TextView userAnswer;
            private TextView correctAnswer;
            private TextView explanation;
        }

        @Override
        public int getCount() {
            try {
                jsonArray = new JSONArray(arraylistAns);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray.length();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.review, parent, false);

            LinearLayout reviewLayout = convertView.findViewById(R.id.reviewResultLayout);
            TextView question = convertView.findViewById(R.id.question);
            TextView userAnswer = convertView.findViewById(R.id.userAnswer);
            TextView correctAnswer = convertView.findViewById(R.id.correctAnswer);
            TextView explanation = convertView.findViewById(R.id.explanation);
            ImageView resultView = convertView.findViewById(R.id.imageResult);

            userAnswer.setVisibility(View.GONE);

            try {
                jsonArray = new JSONArray(arraylistAns);
                Log.d(TAG, "JsonArray Lenth" + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = (JSONObject) jsonArray.get(position);
                        Log.d(TAG, "JsonObject" + jsonObject);

                        question.setText(jsonObject.getString("question"));
                        userAnswer.setText(jsonObject.getString("userAnswer"));
                        correctAnswer.setText(jsonObject.getString("answer"));
                        explanation.setText(jsonObject.getString("comment"));


                        if (explanation.getText().length() == 0 || explanation.getText().toString().isEmpty()) {
                            explanation.setVisibility(View.GONE);
                        } else {
                            explanation.setVisibility(View.VISIBLE);
                        }

                        Log.d(TAG, "All Text" + " " + Question + UserAns + Answer + Comment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if ((userAnswer.getText().toString().equals(correctAnswer.getText().toString()))) {
                userAnswer.setTextColor(Color.parseColor("#FF1A9601"));
                resultView.setBackgroundResource(R.drawable.ic_right);
                correctAns.setVisibility(View.GONE);
            } else {
                userAnswer.setTextColor(Color.parseColor("#f20b3d"));
                resultView.setBackgroundResource(R.drawable.ic_wrong);
                correctAns.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, MasterActivity.class);
        startActivity(intent);
        super.onBackPressed();
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

    public Bitmap takeScreenshotQuelist() {
        View rootView = findViewById(R.id.listviewResult);
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public Bitmap takeScreenshotScore() {
        View rootView = findViewById(R.id.layoutscore);
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        Toast.makeText(ResultActivity.this, "Image Saved at: " + imagePath, Toast.LENGTH_SHORT).show();
    }
}

