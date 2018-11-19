package com.example.extarc.androidpushnotification;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PuzzleFragment extends Fragment {

    private final String TAG = "PuzzleFragment";
    private String questionnaireListStr = null;

    TextView question, explanation;
    int count = 0;
    Button startpuzzle, submitpuzzle;
    RadioGroup rgrouppuzzle;
    RadioButton radioButton;
    LinearLayout ansLayPuzzle;

    RelativeLayout startpuzzleLayout, layoutpuzzle;

    public PuzzleFragment() {
        // Required empty public constructor
    }

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.puzzle_view, container, false);

        question = view.findViewById(R.id.questionPuzzle);
        explanation = view.findViewById(R.id.explanationPuzzle);
        startpuzzle = view.findViewById(R.id.startPuzzle);
        submitpuzzle = view.findViewById(R.id.submitPuzzle);
        rgrouppuzzle = view.findViewById(R.id.rgroupPuzzle);
        ansLayPuzzle = view.findViewById(R.id.ansLayout);

        startpuzzleLayout = view.findViewById(R.id.layoutBtnStartPuzzle);
        layoutpuzzle = view.findViewById(R.id.layoutPuzzle);

        startpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQuestionnaire(1, "PUZ");
                startpuzzleLayout.setVisibility(View.INVISIBLE);
                layoutpuzzle.setVisibility(View.VISIBLE);

            }
        });
        submitpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rgrouppuzzle.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    Toast.makeText(getContext(), "Please Select Answer", Toast.LENGTH_SHORT).show();

                    submitpuzzle.setVisibility(View.VISIBLE);
                }
                else
                {
                    // one of the radio buttons is checked
                    int selectedId = rgrouppuzzle.getCheckedRadioButtonId();  // get selected radio button from radioGroup
                    radioButton = (RadioButton) view.findViewById(selectedId);  // find the radiobutton by returned id
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

                            TextView userAns, correctAns, explanationR;
                            userAns = view.findViewById(R.id.userAnsPuzzle);
                            correctAns = view.findViewById(R.id.correctAnsPuzzle);
                            explanationR = view.findViewById(R.id.explanationPuzzle);

                            userAns.setText(radioButton.getText().toString());
                            correctAns.setText(radioButton.getText().toString());
                            explanationR.setText(arryJobDetails.get(count).getComment());

                            submitpuzzle.setVisibility(View.INVISIBLE);
                            rgrouppuzzle.setVisibility(View.INVISIBLE);
                            ansLayPuzzle.setVisibility(View.VISIBLE);
                            explanationR.setVisibility(View.VISIBLE);

                            if ((userAns.getText().toString().equals(correctAns.getText().toString()))){
                                userAns.setTextColor(Color.parseColor("#40fd1a"));
                            }else {
                                userAns.setTextColor(Color.parseColor("#f20b3d"));
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            submitpuzzle.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.show();
                }

            }
        });
        return view;
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
                    explanation.setText(arryJobDetails.get(count).getComment());

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

}
