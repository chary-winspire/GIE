package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedMathsFragment extends Fragment implements View.OnClickListener {

    Button back, next;
    private Button StartTimer, start, submit;

    private long timeleft = 10000;
    private boolean TimerRunning;
    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();
    private final String TAG = "SpeedMathsFragment";
    private String questionnaireListStr = null;

    RelativeLayout mainscreen;
    LinearLayout resultscreen;

    DonutProgress donutProgress;
    int variable = 0;
    public int visibility = 0, one = 0, two = 0;

    int score = 0;
    int qid = 0;

    public SpeedMathsFragment() {
        // Required empty public constructor
    }

    TextView question;
    EditText answer;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.speedmaths_view, container, false);

        question = view.findViewById(R.id.questionSpdm);
        answer = view.findViewById(R.id.ansSpdm);
        back = view.findViewById(R.id.previousSpdm);
        next = view.findViewById(R.id.nextSpdm);
        StartTimer = view.findViewById(R.id.timerSpdm);
        start = view.findViewById(R.id.startSpdm);
        submit = view.findViewById(R.id.submitSpdm);

        Button One = view.findViewById(R.id.btnOne);
        Button Two = view.findViewById(R.id.btnTwo);
        final Button Three = view.findViewById(R.id.btnThree);
        Button Four = view.findViewById(R.id.btnFour);
        Button Five = view.findViewById(R.id.btnFive);
        Button Six = view.findViewById(R.id.btnSix);
        Button Seven = view.findViewById(R.id.btnSeven);
        Button Eight = view.findViewById(R.id.btnEight);
        Button Nine = view.findViewById(R.id.btnNine);
        Button Zero = view.findViewById(R.id.btnZero);
        Button Clear = view.findViewById(R.id.btnClear);
        Button Dot = view.findViewById(R.id.btnDot);

        mainscreen = view.findViewById(R.id.firstSPdm);
//        resultscreen = view.findViewById(R.id.secSPdm);

        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
        donutProgress.setMax(100);
        donutProgress.setFinishedStrokeColor(Color.parseColor("#FFFB385F"));
        donutProgress.setTextColor(Color.parseColor("#FFFB385F"));
        donutProgress.setKeepScreenOn(true);

        final SeekBar seekBar = view.findViewById(R.id.seekbarSpdm);
        seekBar.setProgress(0);
        seekBar.setMax(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue++;
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getContext(), "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });

        One.setOnClickListener(this);
        Two.setOnClickListener(this);
        Three.setOnClickListener(this);
        Four.setOnClickListener(this);
        Five.setOnClickListener(this);
        Six.setOnClickListener(this);
        Seven.setOnClickListener(this);
        Eight.setOnClickListener(this);
        Nine.setOnClickListener(this);
        Zero.setOnClickListener(this);
        Clear.setOnClickListener(this);
        Dot.setOnClickListener(this);

        final ArrayList<String> userAnsw = new ArrayList<>();
        userAnsw.add(answer.getText().toString());

        StartTimer.setOnClickListener(this);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starttimer();
                getQuestionnaire(1, "SPDM");
                CountControll();
                start.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                seekBar.setProgress(1);
                donutProgress.setVisibility(View.VISIBLE);
                final SharedPreferences shared = getActivity().getSharedPreferences("Score", Context.MODE_PRIVATE);
//                one++;
                new CountDownTimer(60000, 1000) {//countdowntimer
                    int i = 100;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        i = i - 1;
                        donutProgress.setProgress(i);
                    }

                    @Override
                    public void onFinish() {
                        SharedPreferences.Editor editor = shared.edit();//here we are saving the data when the countdown timer will finish and it is saved in shared prefrence file that is defined in onCreate method as score
                        donutProgress.setProgress(0);
                        if (variable == 0) {
                            Intent intent = new Intent(getActivity(), ResultActivity.class);
                            intent.putExtra("correct", two);
                            intent.putExtra("attemp", one);
                            startActivity(intent);
                        }
                    }
                }.start();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getText().toString().isEmpty()) {

                    Toast.makeText(getActivity(), "Please complete and try again", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Are Sure you want to submit");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (arryJobDetails.get(count).getAnswer().equals(answer.getText())) {
                                score++;
                                Log.d("score", "Your score" + score);
                            }

                            Intent intent = new Intent(getActivity(), ResultActivity.class);
                            intent.putExtra("UserAnswers", userAnsw);
                            startActivity(intent);

//                            Intent intent = new Intent(getActivity(), ResultActivity.class);
//                            Bundle b = new Bundle();
//                            b.putInt("score", score); //Your score
//                            intent.putExtras(b); //Put your score to your next Intent
//                            startActivity(intent);

//                            ListView listView = view.findViewById(R.id.listviewSPDM);
//                            CustomAdapterSPDM customAdapter = new CustomAdapterSPDM();
//                            listView.setAdapter(customAdapter);
//                            listView.setVisibility(View.VISIBLE);

//                            Questionnaire questionnaire = new Questionnaire();
//                            questionnaire.setUserAnswer(answer.getText().toString());

//                            RecyclerView recyclerView;
//                            RecyclerView.Adapter customAdapterSPDM;
//                            RecyclerView.LayoutManager mLayoutManager;
//                            recyclerView = view.findViewById(R.id.recyclerSpdm);
//                            recyclerView.setVisibility(View.VISIBLE);
//
//                            mainscreen.setVisibility(View.INVISIBLE);
//                            resultscreen.setVisibility(View.VISIBLE);

//                            TextView userAns, correctAns, question;
//                            userAns = view.findViewById(R.id.spdmUserAns);
//                            correctAns = view.findViewById(R.id.spdmCorrAns);
//                            question = view.findViewById(R.id.spdmQuestion);
//
//                            answer.getText().toString();
//                            userAns.setText(answer.getText().toString());
//                            correctAns.setText(arryJobDetails.get(count).getAnswer());
//                            question.setText(arryJobDetails.get(count).getQuestion());
//
//                            submit.setVisibility(View.INVISIBLE);
//
//                            if ((userAns.getText().toString().equals(correctAns.getText().toString()))){
//                                userAns.setTextColor(Color.parseColor("#40fd1a"));
//                            }else {
//                                userAns.setTextColor(Color.parseColor("#f20b3d"));
//                            }
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            submit.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.show();
                }
            }
        });

//        submit = view.findViewById(R.id.SpSubmit);
//        JobsListView = view.findViewById(R.id.SpdMRecy);
//        JobsListView.setAdapter(lviewAdapter);
//        layoutManager = new LinearLayoutManager(getActivity());
//
//        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayout.HORIZONTAL);
//        JobsListView.setLayoutManager(layoutManager);

        back.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0) {
                    Log.e(TAG, "back input" + count + "");
                    count = count - 1;
                    Log.e(TAG, count + "");
                    Log.e(TAG, arryJobDetails.get(count).getUserAnswer());
                    question.setText(arryJobDetails.get(count).getQuestion());
                    answer.setText(arryJobDetails.get(count).getUserAnswer());
                    Log.e(TAG, "back output" + count + "");

                    seekBar.setProgress(seekBar.getProgress()- 1);
                }
                CountControll();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0 || count == 0) {
                    Log.e(TAG, "next input" + count + "");
                    arryJobDetails.get(count).setUserAnswer(answer.getText().toString());
                    count = count + 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    answer.setText(arryJobDetails.get(count).getUserAnswer());
                    Log.e(TAG, "next output" + count + "");

                    seekBar.setProgress(seekBar.getProgress()+ 1);
                }
                CountControll();
            }
        });

        return view;
    }

    class CustomAdapterSPDM extends BaseAdapter{

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

            question.setText(arryJobDetails.get(count).getQuestion());
            userAns.setText(answer.getText().toString());
            correctAns.setText(arryJobDetails.get(count).getAnswer());
            explanation.setText(arryJobDetails.get(count).getComment());

            if ((userAns.getText().toString().equals(correctAns.getText().toString()))) {
                userAns.setTextColor(Color.parseColor("#40fd1a"));
            } else {
                userAns.setTextColor(Color.parseColor("#f20b3d"));
            }
            return convertView;
        }
    }

    class SPDMadapter extends RecyclerView.Adapter<SPDMadapter.MyViewHolder> {
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView question, userAns, correctAns, explanation;
            public MyViewHolder(View itemView) {
                super(itemView);
                question = itemView.findViewById(R.id.question);
                userAns = itemView.findViewById(R.id.userAnswer);
                correctAns = itemView.findViewById(R.id.correctAnswer);
                explanation = itemView.findViewById(R.id.explanation);
            }
        }
        @NonNull
        @Override
        public SPDMadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }
        @Override
        public void onBindViewHolder(@NonNull SPDMadapter.MyViewHolder holder, int position) {
            holder.question.setText(arryJobDetails.get(count).getQuestion());
            holder.userAns.setText(arryJobDetails.get(count).getUserAnswer());
            holder.correctAns.setText(arryJobDetails.get(count).getAnswer());
            holder.explanation.setText(arryJobDetails.get(count).getComment());
        }
        @Override
        public int getItemCount() {
            return 0;
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

                    question.setText(arryJobDetails.get(count).getQuestion());
//                        lviewAdapter = new CustomSpdMAdapter(getActivity(), arryJobDetails);
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
    public void onPause() {
        super.onPause();
        variable = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnOne:
                answer.setText(answer.getText() + "1");
                break;

            case R.id.btnTwo:
                answer.setText(answer.getText() + "2");
                break;

            case R.id.btnThree:
                answer.setText(answer.getText() + "3");
                break;

            case R.id.btnFour:
                answer.setText(answer.getText() + "4");
                break;

            case R.id.btnFive:
                answer.setText(answer.getText() + "5");
                break;

            case R.id.btnSix:
                answer.setText(answer.getText() + "6");
                break;

            case R.id.btnSeven:
                answer.setText(answer.getText() + "7");
                break;

            case R.id.btnEight:
                answer.setText(answer.getText() + "8");
                break;

            case R.id.btnNine:
                answer.setText(answer.getText() + "9");
                break;

            case R.id.btnZero:
                answer.setText(answer.getText() + "0");
                break;

            case R.id.btnDot:
                answer.setText(answer.getText() + ".");
                break;

            case R.id.btnClear:
                String str = answer.getText().toString();
                if (str != null && str.length() > 0) {
                    str = str.substring(0, str.length() - 1);
                }
                answer.setText(str);
                break;

            case R.id.timerSpdm:
                starttimer();
                break;
        }

    }

    public void starttimer() {

        CountDownTimer countDownTimer = new CountDownTimer(timeleft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                UpdateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        TimerRunning = true;

//        StartStopButton.setText("PAUSE");
    }

    public void UpdateTimer() {
        int minutes = (int) (timeleft / 1000) / 60000;
        int seconds = (int) (timeleft / 1000) % 60000;

        String timelefttext;
        timelefttext = "" + minutes;
        timelefttext += ":";
        if (seconds < 10) timelefttext += "0";
        timelefttext += seconds;

        StartTimer.setText(timelefttext);
    }

    public void CountControll() {
        if (count == 0) {
            back.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        if (count == 9) {
            next.setVisibility(View.INVISIBLE);
            back.setVisibility(View.VISIBLE);
        }
        if (count > 0) {
            back.setVisibility(View.VISIBLE);
        }
        if (count <= 8) {
            next.setVisibility(View.VISIBLE);
        }
    }
}
