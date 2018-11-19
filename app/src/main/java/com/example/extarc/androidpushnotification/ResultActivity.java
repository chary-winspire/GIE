package com.example.extarc.androidpushnotification;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.extarc.androidpushnotification.Models.Questionnaire;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    TextView correctAns;
    TextView incorrectAns;
    TextView attemptedQue;
    TextView Totalscore;
    TextView you;
    int correct = 0, incorrect = 0, attempted = 0, score = 0, your = 0;
    int x = 0;
    private static final int def = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//        Bundle bundle = getIntent().getExtras();
//        int score = bundle.getInt("score");

        Intent intent = getIntent();
        correct = intent.getIntExtra("correct", 0);
        attempted = intent.getIntExtra("attemp", 0);
        String answer = intent.getStringExtra("answer");
        incorrect = attempted - correct;
        score = 10 * correct;

        correctAns = (TextView) findViewById(R.id.correct);
        incorrectAns = (TextView) findViewById(R.id.incorrect);
        attemptedQue = (TextView) findViewById(R.id.attempted);
        Totalscore = (TextView) findViewById(R.id.score);
        you = (TextView) findViewById(R.id.you);

//        ListView listView = findViewById(R.id.listviewSPDM1);
//        AdapterSPDM adapterSPDM = new AdapterSPDM();
//        listView.setAdapter(adapterSPDM);

        you.setText(answer);

        attemptedQue.setText("  " + attempted);
        correctAns.setText(score);
        incorrectAns.setText("  " + incorrect);
        Totalscore.setText("Score  :    " + score);

//        ListView listView = findViewById(R.id.listviewSPDM1);
//        AdapterSPDM customAdapter = new AdapterSPDM();
//        listView.setAdapter(customAdapter);



//        float x1 = (correct * 100) / attempted;
//        if (x1 < 40)
//            you.setText("You Need Improvement");
//        else if (x1 < 70)
//            you.setText("You are an average Quizzer");
//        else if (x1 < 90)
//            you.setText("You are an above average Quizzer ");
//        else
//            you.setText("You are a brilliant  Quizzer ");

    }

    class AdapterSPDM extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Intent intent = getIntent();
//            ArrayList<String> answers = new ArrayList<>();
            String answers = String.valueOf(intent.getStringArrayListExtra("userAnswers"));

            you = findViewById(R.id.you);
            you.setText(answers);

            return convertView;
        }
    }
}
