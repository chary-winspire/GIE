package com.example.extarc.androidpushnotification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener {

    LinearLayout motivation, spdm, gk, puzzle, poll, tofw, reminder;


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        motivation = view.findViewById(R.id.cateMotivation);
        spdm = view.findViewById(R.id.cateSpdm);
        gk = view.findViewById(R.id.cateGk);
        puzzle = view.findViewById(R.id.catePuzzle);
        poll = view.findViewById(R.id.catePoll);
        tofw = view.findViewById(R.id.cateTofw);
        reminder = view.findViewById(R.id.cateReminder);

        motivation.setOnClickListener(this);
        spdm.setOnClickListener(this);
        gk.setOnClickListener(this);
        puzzle.setOnClickListener(this);
        poll.setOnClickListener(this);
        tofw.setOnClickListener(this);
        reminder.setOnClickListener(this);

    return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cateMotivation:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new MotivationFragment()).commit();
                    ((MasterActivity) getActivity()).getSupportActionBar().setTitle("Motivation");
                }
                break;

            case R.id.cateSpdm:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new SpeedMathsFragment()).commit();
                    ((MasterActivity) getActivity()).getSupportActionBar().setTitle("Speed Maths");
                }
                break;

            case R.id.cateGk:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new GKFragment()).commit();
                    ((MasterActivity) getActivity()).getSupportActionBar().setTitle("GK");
                }
                break;

            case R.id.catePuzzle:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new PuzzleFragment()).commit();
                    ((MasterActivity) getActivity()).getSupportActionBar().setTitle("Puzzle");
                }
                break;
            case R.id.cateReminder:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new ReminderFragment()).commit();
                    ((MasterActivity) getActivity()).getSupportActionBar().setTitle("Reminder");
                }
                break;
        }
    }

}
