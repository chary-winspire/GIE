package com.example.extarc.androidpushnotification;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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
public class CategoriesFragment extends Fragment implements View.OnClickListener {

    LinearLayout motivation, spdm, gk, puzzle, poll, tofw, reminder, wordpower;

    ScrollView scrollView;
//    RelativeLayout reminder;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(30, 16, 30, 6);
        toolbar.setLayoutParams(layoutParams);
        toolbartitle2.setVisibility(View.GONE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle.setText("Winspire Go");
        toolbar.setTitleTextColor(getResources().getColor(R.color.Black));
        toolbar.setBackgroundColor(getResources().getColor(R.color.White));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        motivation = view.findViewById(R.id.cateMotivation);
        spdm = view.findViewById(R.id.cateSpdm);
        gk = view.findViewById(R.id.cateGk);
        puzzle = view.findViewById(R.id.catePuzzle);
        tofw = view.findViewById(R.id.cateTofw);
        reminder = view.findViewById(R.id.cateReminder);
        wordpower = view.findViewById(R.id.cateWord);

        motivation.setOnClickListener(this);
        spdm.setOnClickListener(this);
        gk.setOnClickListener(this);
        puzzle.setOnClickListener(this);
        tofw.setOnClickListener(this);
        reminder.setOnClickListener(this);
        wordpower.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cateMotivation:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new MotivationFragment()).commit();
                }
                break;

            case R.id.cateSpdm:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new SpeedMathsFragment()).commit();
                }
                break;

            case R.id.cateGk:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new GKFragment()).commit();
                }
                break;

            case R.id.catePuzzle:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new PuzzleFragment()).commit();
                }
                break;
            case R.id.cateReminder:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new ReminderFragment()).commit();
                }
                break;
            case R.id.cateTofw:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new TopicFragment()).commit();
                }
                break;
            case R.id.cateWord:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new WordPowerFragment()).commit();
                }
                break;
        }
    }

}
