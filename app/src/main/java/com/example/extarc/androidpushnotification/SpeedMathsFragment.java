package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
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
public class SpeedMathsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    Button back, next;
    private Button start, submit;
    TextView StartTimer;
    TextView queNo;
    TextView questionNo;

    RelativeLayout mainlayoutSpdm, startSpdmlayout;

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();
    private final String TAG = "SpeedMathsFragment";
    private String questionnaireListStr = null;

    CountDownTimer countDownTimer;
    private static final String FORMAT = "%02d:%02d:%02d";
    Animation animation, queAnim;

    public SpeedMathsFragment() {
        // Required empty public constructor
    }

    TextView question;
    RadioButton option1, option2, option3, option4;
    RadioGroup radioGroupSPDM;
    int count = 0;
    int questionNu = 1;
    int ID = 1;
    public SharedPreferences preferences;
    int Score = 0;
    int Wrong = 0;
    int NotAttempted = 0;

    SeekBar seekbarTimer;
    long timeLeft;

    String SERVICE_URI = ApplicationConstants.SERVER_PATH;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.speedmaths_view, container, false);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        appBarLayout.setLayoutParams(layoutParams);
//        toolbar.setTitle("Speed Maths");
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.INVISIBLE);
        toolbartitle2.setText("Speed Math");
        toolbartitle2.setTextColor(getResources().getColor(R.color.Black));
        toolbar.setBackgroundColor(getResources().getColor(R.color.SPDM_Toolbar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
//        toolbar.setNavigationIcon(null);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID = Integer.valueOf(preferences.getString(Constants.SPEED_MATHS, "1"));
        back = myview.findViewById(R.id.previousSpdm);
        next = myview.findViewById(R.id.nextSpdm);
        StartTimer = myview.findViewById(R.id.timerSpdm);
        start = myview.findViewById(R.id.startSpdm);
        submit = myview.findViewById(R.id.submitSpdm);
        queNo = myview.findViewById(R.id.queNo);
        questionNo = myview.findViewById(R.id.questionCount);
        questionNo.setText("1/10");
        questionNu = questionNu++;

        question = myview.findViewById(R.id.questionSpdm);
        option1 = myview.findViewById(R.id.spdmOp1);
        option2 = myview.findViewById(R.id.spdmOp2);
        option3 = myview.findViewById(R.id.spdmOp3);
        option4 = myview.findViewById(R.id.spdmOp4);
        radioGroupSPDM = myview.findViewById(R.id.rgroupSpdm);

        radioGroupSPDM.setOnCheckedChangeListener(this);

//        donutProgress = (DonutProgress) myview.findViewById(R.id.donut_progress);
//        donutProgress.setMax(100);
//        donutProgress.setFinishedStrokeColor(Color.parseColor("#FFFB385F"));
//        donutProgress.setTextColor(Color.parseColor("#FFFB385F"));
//        donutProgress.setKeepScreenOn(true);

        mainlayoutSpdm = myview.findViewById(R.id.mainSpdmLayout);
        startSpdmlayout = myview.findViewById(R.id.layoutBtnStartSpdm);

        final SeekBar seekBar = myview.findViewById(R.id.seekbarSpdm);
        seekBar.setProgress(0);
        seekBar.setMax(10);
        seekBar.setClickable(false);
        seekBar.setFocusable(false);

        seekbarTimer = myview.findViewById(R.id.seekbarSpdmtimer);
        seekbarTimer.getThumb().mutate().setAlpha(0);

        startSpdmlayout.setVisibility(View.VISIBLE);
        mainlayoutSpdm.setVisibility(View.GONE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toolbar.setNavigationIcon(null);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                toolbartitle.setVisibility(View.INVISIBLE);
                toolbartitle2.setVisibility(View.VISIBLE);
                toolbartitle2.setText("Speed Math");
                fab.setVisibility(View.GONE);

                Animation startanim = AnimationUtils.loadAnimation(getContext(), R.anim.lefttorightin);
                getQuestionnaire(ID, "SPDM");
                Animation();
                startQueAnimation();
                CountControll();
                startSpdmlayout.setVisibility(View.GONE);
                mainlayoutSpdm.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                seekBar.setProgress(1);
                startcountdown();

//                if (question.getText().toString().isEmpty()) {
////                    start.startAnimation(startanim);
//                    start.setText("Loding...");
//                    start.setAllCaps(false);
//                }else {
//                    Animation();
//                    startQueAnimation();
//                    CountControll();
//                    startSpdmlayout.setVisibility(View.GONE);
//                    mainlayoutSpdm.setVisibility(View.VISIBLE);
//                    submit.setVisibility(View.INVISIBLE);
//                    seekBar.setProgress(1);
//                    startcountdown();
//                }
            }
        });

        back.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQueAnimation();
                Animation();
                if (count > 0) {
                    count = count - 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());

                    seekBar.setProgress(seekBar.getProgress() - 1);

                    Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                    if (arryJobDetails.get(count).getUserAnswer() != null && arryJobDetails.get(count).getUserAnswer().length() > 0) {
                        if (option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                            resetColor();
                            radioGroupSPDM.clearCheck();
                            option1.setChecked(true);
                            Log.i(TAG, " *** getQuestionnaire started" + option1.isChecked());
                        } else if (option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.setBackgroundResource(android.R.drawable.btn_default);
                            radioGroupSPDM.clearCheck();
                            option2.setChecked(true);
                        } else if (option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.setBackgroundResource(android.R.drawable.btn_default);
                            radioGroupSPDM.clearCheck();
                            option3.setChecked(true);
                        } else if (option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.setBackgroundResource(android.R.drawable.btn_default);
                            radioGroupSPDM.clearCheck();
                            option4.setChecked(true);
                        }
                    }
                }
                CountControll();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startQueAnimation();
                Animation();
                Log.i(TAG, " *** getQuestionnaire started" + radioGroupSPDM.getCheckedRadioButtonId());
                if (count > 0 || count == 0) {
                    String userAnswer = null;
                    if (option1.isChecked()) {
                        userAnswer = option1.getText().toString();
                    } else if (option2.isChecked()) {
                        userAnswer = option2.getText().toString();
                    } else if (option3.isChecked()) {
                        userAnswer = option3.getText().toString();
                    } else if (option4.isChecked()) {
                        userAnswer = option4.getText().toString();
                    }

                    questionNu = questionNu + 1;
                    questionNo.setText(questionNu + "/" + arryJobDetails.size());

                    if (userAnswer != null) {
                        if (arryJobDetails.get(count).getAnswer().equalsIgnoreCase(userAnswer)) {
                            Score = Score + 1;
                        } else if (!arryJobDetails.get(count).getAnswer().equalsIgnoreCase(userAnswer)) {
                            Wrong = Wrong + 1;
                        }
                    } else {
                        NotAttempted = NotAttempted + 1;
                    }


                    arryJobDetails.get(count).setUserAnswer(userAnswer);
                    Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());

                    count = count + 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());

                    seekBar.setProgress(seekBar.getProgress() + 1);

                    Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                    if (arryJobDetails.get(count).getUserAnswer() != null && arryJobDetails.get(count).getUserAnswer().length() > 0) {
                        if (option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.clearCheck();
                            option1.setChecked(true);
                        } else if (option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.clearCheck();
                            option2.setChecked(true);
                        } else if (option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.clearCheck();
                            option3.setChecked(true);
                        } else if (option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroupSPDM.clearCheck();
                            option4.setChecked(true);
                        }
                    } else {
                        startQueAnimation();
                        Animation();
                        resetColor();
                        radioGroupSPDM.clearCheck();
                        option1.setChecked(false);
                        option2.setChecked(false);
                        option3.setChecked(false);
                        option4.setChecked(false);
                    }
//                    if (userAnswer.equalsIgnoreCase(arryJobDetails.get(count).getAnswer())){
//                        Score++;
//                    }else if (!userAnswer.equalsIgnoreCase(arryJobDetails.get(count).getAnswer())){
//                        Wrong++;
//                    }else if (userAnswer.isEmpty()){
//                        NotAttempt++;
//                    }
                }
                CountControll();
            }
        });

        radioGroupSPDM.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (count == 9) {
                    if (option1.isChecked()) {
                        option1.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        submit.performClick();
                    } else if (option2.isChecked()) {
                        option2.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        submit.performClick();
                    } else if (option3.isChecked()) {
                        option3.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        submit.performClick();
                    } else if (option4.isChecked()) {
                        option4.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        submit.performClick();
                    }
                } else {
                    if (option1.isChecked()) {
                        option1.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        nextQueDelay();
                    } else if (option2.isChecked()) {
                        option2.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        nextQueDelay();
                    } else if (option3.isChecked()) {
                        option3.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        nextQueDelay();
                    } else if (option4.isChecked()) {
                        option4.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                        nextQueDelay();
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
//                builder.setTitle("Are Sure you want to submit");
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {

                String userAnswer = null;
                if (option1.isChecked()) {
                    userAnswer = option1.getText().toString();
                } else if (option2.isChecked()) {
                    userAnswer = option2.getText().toString();
                } else if (option3.isChecked()) {
                    userAnswer = option3.getText().toString();
                } else if (option4.isChecked()) {
                    userAnswer = option4.getText().toString();
                }

                if (userAnswer != null) {
                    if (arryJobDetails.get(count).getAnswer().equalsIgnoreCase(userAnswer)) {
                        Score = Score + 1;
                    } else if (!arryJobDetails.get(count).getAnswer().equalsIgnoreCase(userAnswer)) {
                        Wrong = Wrong + 1;
                    }
                } else {
                    NotAttempted = NotAttempted + 1;
                }

//                        arryJobDetails.get(9).setUserAnswer(userAnswer);
//                        ListView listView = Objects.requireNonNull(getActivity()).findViewById(R.id.listviewSPDM);
//                        CustomAdapterSPDM customAdapter = new CustomAdapterSPDM(arryJobDetails);
//                        listView.setAdapter(customAdapter);
//                        listView.setVisibility(View.VISIBLE);

                submit.setVisibility(View.INVISIBLE);
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                Log.d(TAG, "arrayjobdetails" + gson.toJson(arryJobDetails));
                intent.putExtra("userArray", gson.toJson(arryJobDetails));
//                        intent.putExtra("AnswersSPDM",arryJobDetails.get(count).getAnswer());
//                        intent.putExtra("UserAnsSPDM",userAnswer);
//                        intent.putExtra("CommentsSPDM",arryJobDetails.get(count).getComment());

                intent.putExtra("notattempted", NotAttempted);
                intent.putExtra("score", Score);
                intent.putExtra("wrong", Wrong);
                intent.putExtra("timeleftMilli", timeLeft);
                startActivity(intent);

            }
        });
//                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
////
//                    }
//                });
//                builder.show();
//            }
//
//        });

        return myview;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rgroupSpdm:
                if (option1.isChecked()) {
                    option1.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option3.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option4.setBackgroundColor(getResources().getColor(R.color.SPDM));
                } else if (option2.isChecked()) {
                    option2.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option1.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option3.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option4.setBackgroundColor(getResources().getColor(R.color.SPDM));
                } else if (option3.isChecked()) {
                    option3.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option1.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option4.setBackgroundColor(getResources().getColor(R.color.SPDM));
                } else if (option4.isChecked()) {
                    option4.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option3.setBackgroundColor(getResources().getColor(R.color.SPDM));
                    option1.setBackgroundColor(getResources().getColor(R.color.SPDM));
                }
                break;
        }
    }

    class CustomAdapterSPDM extends BaseAdapter {

        private ArrayList<Questionnaire> arrayList;
        private Context context;

        public CustomAdapterSPDM(ArrayList<Questionnaire> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.review, null);

            TextView question = convertView.findViewById(R.id.question);
            TextView userAns = convertView.findViewById(R.id.userAnswer);
            TextView correctAns = convertView.findViewById(R.id.correctAnswer);
            TextView explanation = convertView.findViewById(R.id.explanation);

            question.setText(arrayList.get(position).getQuestion());
            userAns.setText(arrayList.get(position).getUserAnswer());
            correctAns.setText(arrayList.get(position).getAnswer());
            explanation.setText(arrayList.get(position).getComment());

            if ((userAns.getText().toString().equals(correctAns.getText().toString()))) {
                userAns.setTextColor(Color.parseColor("#40fd1a"));
            } else {
                userAns.setTextColor(Color.parseColor("#f20b3d"));
            }

            return convertView;
        }

    }

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
                    questionNo.setText("1/10");
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());
                    Animation();
                    startQueAnimation();
                }
            } else {
                Log.i(TAG, " List completed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.isConnected() && netInfo.isAvailable()) {

            } else {

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

        }
    }

    public void CountControll() {
        if (count == 0) {
            back.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
        }
        if (count == 9) {
            next.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
        }
        if (count > 0) {
            back.setVisibility(View.INVISIBLE);
        }
        if (count <= 8) {
            next.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
        }
    }

    public void resetColor() {
        option1.setBackgroundColor(getResources().getColor(R.color.SPDM));
        option2.setBackgroundColor(getResources().getColor(R.color.SPDM));
        option3.setBackgroundColor(getResources().getColor(R.color.SPDM));
        option4.setBackgroundColor(getResources().getColor(R.color.SPDM));
    }

    public void Animation() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.fall_down);
        option1.startAnimation(animation);
        option2.startAnimation(animation);
        option3.startAnimation(animation);
        option4.startAnimation(animation);
    }

    public void startQueAnimation() {
        queAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        question.startAnimation(queAnim);
    }

    public void startcountdown() {
        countDownTimer = new CountDownTimer(120000, 1000) {
            int i = 1000;

            @Override
            public void onTick(final long millSecondsLeftToFinish) {
                long seconds = millSecondsLeftToFinish / 6000;
                i = i - 1;
                int progress = (int) (millSecondsLeftToFinish / 1200);
                seekbarTimer.setProgress(progress);
                timeLeft = millSecondsLeftToFinish;
//                donutProgress.setProgress(i);

                StartTimer.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millSecondsLeftToFinish),
                        TimeUnit.MILLISECONDS.toMinutes(millSecondsLeftToFinish) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecondsLeftToFinish)),
                        TimeUnit.MILLISECONDS.toSeconds(millSecondsLeftToFinish) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millSecondsLeftToFinish))));
            }

            @Override
            public void onFinish() {
                StartTimer.setText("Done!");
                seekbarTimer.setProgress(0);
                if (mainlayoutSpdm.isShown()){
                    submit.performClick();
                }else {
                    countDownTimer.cancel();
                }
//                donutProgress.setProgress(0);
            }
        };
        countDownTimer.start();

    }

    public void nextQueDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                next.performClick();
            }
        }, 150);
    }

}
