package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.drawerLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.fab;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbar;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle2;


/**
 * A simple {@link Fragment} subclass.
 */
public class GKFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private final String TAG = "GKFragment";
    private String questionnaireListStr = null;

    TextView question;
    RelativeLayout quePanel;
    RadioButton option1, option2, option3, option4;
    LinearLayout ansLayoutGK;
    RelativeLayout mainGKlayout, startGKlayout;
    Button next, previous, submit, start;
    int count = 0;
    int ID=1;
    RadioGroup radioGroup;
    Animation ansAnim, queAnim;
    public SharedPreferences preferences;
    int Score = 0;
    int Wrong = 0;
    int NotAttempted = 0;

    public GKFragment() {

        // Required empty public constructor
    }

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.gk_view, container, false);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, 0, 0 , 0);
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID= Integer.valueOf(preferences.getString(Constants.GK,"1"));
        toolbar.setLayoutParams(layoutParams);
//        toolbar.setTitle("General Knowledge");
        toolbar.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
//        bottomNavigation.setVisibility(View.GONE);
//        fab.setVisibility(View.VISIBLE);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        question = myview.findViewById(R.id.questionGk);
        option1 = myview.findViewById(R.id.gkOp1);
        option2 = myview.findViewById(R.id.gkOp2);
        option3 = myview.findViewById(R.id.gkOp3);
        option4 = myview.findViewById(R.id.gkOp4);
        ansLayoutGK = myview.findViewById(R.id.ansLayoutGK);
        quePanel = myview.findViewById(R.id.questionGkpanel);

        radioGroup = myview.findViewById(R.id.rgroupGk);
        radioGroup.setOnCheckedChangeListener(this);
        mainGKlayout = myview.findViewById(R.id.mainGKlayout);
        startGKlayout = myview.findViewById(R.id.layoutBtnStartGK);

        next = myview.findViewById(R.id.nextGk);
        previous = myview.findViewById(R.id.previousGk);
        submit = myview.findViewById(R.id.submitGk);
        start = myview.findViewById(R.id.startGK);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolbartitle.setVisibility(View.INVISIBLE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("GK Digest");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));
        toolbar.setNavigationIcon(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fab.setVisibility(View.GONE);
        startGKlayout.setVisibility(View.INVISIBLE);
        mainGKlayout.setVisibility(View.VISIBLE);
        getQuestionnaire(ID, "GK");
        startAnimation();
        startQueAnimation();

        previous.setVisibility(View.INVISIBLE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQueAnimation();
                startAnimation();
                if (count > 0) {
                    count = count - 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());
                    Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                    if (arryJobDetails.get(count).getUserAnswer() != null && arryJobDetails.get(count).getUserAnswer().length() > 0) {
                        if (option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                            resetColor();
                            radioGroup.clearCheck();
                            option1.setChecked(true);

                            Log.i(TAG, " *** getQuestionnaire started" + option1.isChecked());
                        } else if (option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option2.setChecked(true);
                        } else if (option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option3.setChecked(true);
                        } else if (option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
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
                startAnimation();
                Log.i(TAG, " *** getQuestionnaire started" + radioGroup.getCheckedRadioButtonId());
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

                    Log.i(TAG, " *** getQuestionnaire started" + arryJobDetails.get(count).getUserAnswer());
                    if (arryJobDetails.get(count).getUserAnswer() != null && arryJobDetails.get(count).getUserAnswer().length() > 0) {
                        if (option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option1.setChecked(true);
                        } else if (option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option2.setChecked(true);
                        } else if (option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option3.setChecked(true);
                        } else if (option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())) {
                            resetColor();
                            radioGroup.clearCheck();
                            option4.setChecked(true);
                        }
                    } else {
                        startAnimation();
                        startQueAnimation();
                        resetColor();
                        radioGroup.clearCheck();
                        option1.setChecked(false);
                        option2.setChecked(false);
                        option3.setChecked(false);
                        option4.setChecked(false);
                    }
                }
                CountControll();
            }


//                    int selectedId = Answer.getCheckedRadioButtonId();
//                    radioButton = view.findViewById(selectedId);
//                    Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
//
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are Sure you want to submit");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

//                        arryJobDetails.get(4).setUserAnswer(userAnswer);
//                        ListView listView = Objects.requireNonNull(getActivity()).findViewById(R.id.listviewGK);
//                        CustomAdapterGK customAdapter = new CustomAdapterGK(arryJobDetails);
//                        listView.setAdapter(customAdapter);
//                        listView.setVisibility(View.VISIBLE);

                        mainGKlayout.setVisibility(View.INVISIBLE);

                        submit.setVisibility(View.INVISIBLE);
                        ansLayoutGK.setVisibility(View.VISIBLE);

                        Gson gson = new Gson();
                        Intent intent = new Intent(getActivity(), ResultActivity.class);
                        Log.d(TAG, "arrayjobdetails" +  gson.toJson(arryJobDetails));
                        intent.putExtra("userArray",  gson.toJson(arryJobDetails));
                        intent.putExtra("notattempted", NotAttempted);
                        intent.putExtra("score", Score);
                        intent.putExtra("wrong", Wrong);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                            submit.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();
            }
        });
        return myview;

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rgroupGk:
                if (option1.isChecked()) {
                    option1.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                } else if (option2.isChecked()) {
                    option2.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option1.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                } else if (option3.isChecked()) {
                    option3.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option1.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option4.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                } else if (option4.isChecked()) {
                    option4.setBackgroundColor(getResources().getColor(R.color.myGreen));
                    option2.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option3.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                    option1.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
                }
                break;
        }
    }

    class CustomAdapterGK extends BaseAdapter {
        private ArrayList<Questionnaire> arrayList;
        private Context context;

        public CustomAdapterGK(ArrayList<Questionnaire> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return 5;
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
                    Log.e(TAG, "jsonalljobs" + jsonAllJobs);
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
                    setQuestionView();
                    startAnimation();
                    startQueAnimation();

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

    public void CountControll() {
        if (count == 0) {
            previous.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        if (count == 4) {
            next.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        if (count > 0) {
            previous.setVisibility(View.INVISIBLE);
        }
        if (count <= 3) {
            next.setVisibility(View.VISIBLE);
            submit.setVisibility(View.INVISIBLE);
        }
    }

    private void setQuestionView() {
        question.setText(arryJobDetails.get(count).getQuestion());
        option1.setText(arryJobDetails.get(count).getOption1());
        option2.setText(arryJobDetails.get(count).getOption2());
        option3.setText(arryJobDetails.get(count).getOption3());
        option4.setText(arryJobDetails.get(count).getOption4());
    }

    public void resetColor() {
        option1.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
        option2.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
        option3.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
        option4.setBackgroundColor(getResources().getColor(R.color.GK_Toolbar));
    }
    public void startAnimation(){
        ansAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fall_down);
        queAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        option1.startAnimation(ansAnim);
        option2.startAnimation(ansAnim);
        option3.startAnimation(ansAnim);
        option4.startAnimation(ansAnim);
        question.startAnimation(queAnim);
    }
    public void startQueAnimation(){
        queAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        question.startAnimation(queAnim);
    }
}
