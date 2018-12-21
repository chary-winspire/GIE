package com.example.extarc.androidpushnotification;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import static android.os.Environment.DIRECTORY_PICTURES;

public class ResultActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "ResultActivity";

    TextView correctAns;
    TextView incorrectAns;
    TextView attemptedQue;
    TextView Totalscore;
    TextView NotAttempted;
    int correct = 0, incorrect = 0, attempted = 0, score = 0, notAttempted = 0, your = 0;
    LinearLayout attemptedLayout, correctLayout, incorrectLayout, notattempLayout;
    Button tryagain;
    TextView resultText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getWindow());
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.LiteGreen));
        }

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        incorrect = intent.getIntExtra("wrong", 0);
        notAttempted = intent.getIntExtra("notattempted", 0);
        arraylistAns = intent.getStringExtra("userArray");
        Log.d(TAG, "arraylistAns" + arraylistAns);

        correctAns = (TextView) findViewById(R.id.correct);
        incorrectAns = (TextView) findViewById(R.id.incorrect);
        attemptedQue = (TextView) findViewById(R.id.attempted);
        Totalscore = (TextView) findViewById(R.id.score);
        NotAttempted = (TextView) findViewById(R.id.notAttempted);
        tryagain = (Button) findViewById(R.id.tryagainResult);
        resultText = (TextView) findViewById(R.id.resultText);

        attemptedLayout = (LinearLayout) findViewById(R.id.attemptedlayout);
        correctLayout = (LinearLayout) findViewById(R.id.correctlayout);
        incorrectLayout = (LinearLayout) findViewById(R.id.incorrectlayout);
        notattempLayout = (LinearLayout) findViewById(R.id.notattemptedlayout);

        bottomNavigationView = findViewById(R.id.BottombarSecond);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        layoutscore = findViewById(R.id.layoutscore);
        TitleResult = findViewById(R.id.titleresult);

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
            Totalscore.setText("Total Score  :    " + your);
            attemptedQue.setText("  " + attempted + " / " + "10");
            NotAttempted.setText("  " + notAttempted + " / " + "10");
            pieData.add(new SliceValue(notAttempted, Color.BLUE).setLabel("Not Attempted"));
            pieData.add(new SliceValue(score, Color.GREEN).setLabel("Correct Answers"));
            pieData.add(new SliceValue(incorrect, Color.RED).setLabel("Incorrect Answers"));

            TitleResult.setBackgroundResource(R.color.SPDM_Toolbar);
            bottomNavigationView.setBackgroundResource(R.color.SPDM_Toolbar);
            layoutscore.setBackgroundResource(R.color.SPDM_normal);
        } else {
            your = score * 20;
            Totalscore.setText("Total Score: " + your);
            attemptedQue.setText("  " + attempted + " / " + "5");
            NotAttempted.setText("  " + notAttempted + " / " + "5");
            pieData.add(new SliceValue(notAttempted * 2, Color.BLUE).setLabel("Not Attempted"));
            pieData.add(new SliceValue(score * 2, Color.GREEN).setLabel("Correct Answers"));
            pieData.add(new SliceValue(incorrect * 2, Color.RED).setLabel("Incorrect Answers"));

            TitleResult.setBackgroundResource(R.color.GK_Toolbar);
            bottomNavigationView.setBackgroundResource(R.color.GK_Toolbar);
            layoutscore.setBackgroundResource(R.color.GK_normal);
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
        }else resultText.setText("Your Score below 20");

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

                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                if (layoutscore.isShown()) {
                    listView.startAnimation(slideUp);
                    listView.setVisibility(View.VISIBLE);
                    layoutscore.setVisibility(View.INVISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.b1Review).setTitle("Score Card");
                } else {
                    listView.startAnimation(slideDown);
                    listView.setVisibility(View.INVISIBLE);
                    layoutscore.setVisibility(View.VISIBLE);
                    bottomNavigationView.getMenu().findItem(R.id.b1Review).setTitle("Review");
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
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
            LinearLayout correctAnslayout = convertView.findViewById(R.id.correctAnslayoutResult);
            TextView question = convertView.findViewById(R.id.question);
            TextView userAnswer = convertView.findViewById(R.id.userAnswer);
            TextView correctAnswer = convertView.findViewById(R.id.correctAnswer);
            TextView explanation = convertView.findViewById(R.id.explanation);

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
                userAnswer.setTextColor(Color.parseColor("#40fd1a"));
                correctAnslayout.setVisibility(View.GONE);
            } else {
                userAnswer.setTextColor(Color.parseColor("#f20b3d"));
                correctAnslayout.setVisibility(View.VISIBLE);
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

