package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class GKFragment extends Fragment {

    List<Questionnaire> quesList;

    private final String TAG = "GKFragment";
    private String questionnaireListStr = null;

    TextView question;
    RadioButton option1, option2, option3, option4;
    LinearLayout ansLayoutGK;
    RelativeLayout mainGKlayout;
    Button next, previous, submit;
    int count = 0;

    int score = 0;
    int queid = 0;
    RadioGroup radioGroup;

    public GKFragment() {

        // Required empty public constructor
    }

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.gk_view, container, false);

        question = myview.findViewById(R.id.questionGk);
        option1 = myview.findViewById(R.id.gkOp1);
        option2 = myview.findViewById(R.id.gkOp2);
        option3 = myview.findViewById(R.id.gkOp3);
        option4 = myview.findViewById(R.id.gkOp4);
        ansLayoutGK = myview.findViewById(R.id.ansLayoutGK);

        radioGroup = myview.findViewById(R.id.rgroupGk);

        mainGKlayout = myview.findViewById(R.id.mainGKlayout);

        next = myview.findViewById(R.id.nextGk);
        previous = myview.findViewById(R.id.previousGk);
        submit = myview.findViewById(R.id.submitGk);


//        int selectedId = rgroupGK.getCheckedRadioButtonId();
//        radioButton = view.findViewById(selectedId);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are Sure you want to submit");
//                    builder.setMessage("Your selected answer is: " + radioButton.getText().toString());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
////                            Toast.makeText(getContext(), radioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
//                            for (int i = 0; i < rgroupGK.getChildCount(); i++) {
//                                rgroupGK.getChildAt(i).setEnabled(false);
//                            }
                        String userAnswer=null;
                        if(option1.isChecked()){
                            userAnswer= option1.getText().toString();
                        } else if(option2.isChecked()){
                            userAnswer= option2.getText().toString();
                        }else if(option3.isChecked()){
                            userAnswer= option3.getText().toString();
                        }else if(option4.isChecked()){
                            userAnswer= option4.getText().toString();
                        }

                        arryJobDetails.get(4).setUserAnswer(userAnswer);
                        ListView listView = Objects.requireNonNull(getActivity()).findViewById(R.id.listviewGK);
                        CustomAdapterGK customAdapter = new CustomAdapterGK(arryJobDetails);
                        listView.setAdapter(customAdapter);
                        listView.setVisibility(View.VISIBLE);

                        mainGKlayout.setVisibility(View.INVISIBLE);

//                            TextView userAns, correctAns;
//                            userAns = view.findViewById(R.id.userAnsGK);
//                            correctAns = view.findViewById(R.id.correctAnsGK);
//
//                            userAns.setText(radioButton.getText().toString());
//                            correctAns.setText(arryJobDetails.get(count).getAnswer());

                        submit.setVisibility(View.INVISIBLE);
                        ansLayoutGK.setVisibility(View.VISIBLE);

//                            if ((userAns.getText().toString().equals(correctAns.getText().toString()))){
//                                userAns.setTextColor(Color.parseColor("#40fd1a"));
//                            }else {
//                                userAns.setTextColor(Color.parseColor("#f20b3d"));
//                            }
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

        previous.setVisibility(View.INVISIBLE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (count > 0) {
                if (count > 0) {


                    count = count - 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());
                    Log.i(TAG, " *** getQuestionnaire started"+arryJobDetails.get(count).getUserAnswer());
                    if(arryJobDetails.get(count).getUserAnswer()!=null && arryJobDetails.get(count).getUserAnswer().length()>0 ){
                        if(option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                            Log.i(TAG, " *** getQuestionnaire started"+arryJobDetails.get(count).getUserAnswer());
                            radioGroup.clearCheck();
                            option1.setChecked(true);

                            Log.i(TAG, " *** getQuestionnaire started"+   option1.isChecked());
                        } else if(option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                            radioGroup.clearCheck();
                            option2.setChecked(true);
                        }else if(option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                            radioGroup.clearCheck();
                            option3.setChecked(true);
                        }else if(option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
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
                Log.i(TAG, " *** getQuestionnaire started"+radioGroup.getCheckedRadioButtonId());
//                if (count > 0 || count == 0) {
                if (count > 0 || count == 0) {
                    String userAnswer=null;
                    if(option1.isChecked()){
                        userAnswer= option1.getText().toString();
                    } else if(option2.isChecked()){
                        userAnswer= option2.getText().toString();
                    }else if(option3.isChecked()){
                        userAnswer= option3.getText().toString();
                    }else if(option4.isChecked()){
                        userAnswer= option4.getText().toString();
                    }

                    arryJobDetails.get(count).setUserAnswer(userAnswer);
                    Log.i(TAG, " *** getQuestionnaire started"+  arryJobDetails.get(count).getUserAnswer());
                    count = count + 1;
                    question.setText(arryJobDetails.get(count).getQuestion());
                    option1.setText(arryJobDetails.get(count).getOption1());
                    option2.setText(arryJobDetails.get(count).getOption2());
                    option3.setText(arryJobDetails.get(count).getOption3());
                    option4.setText(arryJobDetails.get(count).getOption4());

                    Log.i(TAG, " *** getQuestionnaire started"+  arryJobDetails.get(count).getUserAnswer());
                    if(arryJobDetails.get(count).getUserAnswer()!=null && arryJobDetails.get(count).getUserAnswer().length()>0 ){
                       if(option1.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                           radioGroup.clearCheck();
                           option1.setChecked(true);
                       } else if(option2.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                           radioGroup.clearCheck();
                           option2.setChecked(true);
                       }else if(option3.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                           radioGroup.clearCheck();
                           option3.setChecked(true);
                       }else if(option4.getText().toString().equalsIgnoreCase(arryJobDetails.get(count).getUserAnswer())){
                           radioGroup.clearCheck();
                           option4.setChecked(true);
                       }

                    }else{
                        radioGroup.clearCheck();
                        option1.setChecked(false);
                        option2.setChecked(false);option3.setChecked(false);option4.setChecked(false);

                    }

//                    int selectedId = Answer.getCheckedRadioButtonId();
//                    radioButton = view.findViewById(selectedId);
//                    Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                }

//
//
//                count = count + 1;
//
//                setQuestionView();
                CountControll();




            }


//                    int selectedId = Answer.getCheckedRadioButtonId();
//                    radioButton = view.findViewById(selectedId);
//                    Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
//
        });
        getQuestionnaire(1, "GK");
        return myview;

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
                    setQuestionView();

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
            previous.setVisibility(View.VISIBLE);
        }
        if (count > 0) {
            previous.setVisibility(View.VISIBLE);
        }
        if (count <= 3) {
            next.setVisibility(View.VISIBLE);
        }
    }

    private void setQuestionView() {
        question.setText(arryJobDetails.get(count).getQuestion());
        option1.setText(arryJobDetails.get(count).getOption1());
        option2.setText(arryJobDetails.get(count).getOption2());
        option3.setText(arryJobDetails.get(count).getOption3());
        option4.setText(arryJobDetails.get(count).getOption4());
        queid++;
    }
}
