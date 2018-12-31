package com.example.extarc.androidpushnotification;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.drawerLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.fab;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbar;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle;
import static com.example.extarc.androidpushnotification.MasterActivity.toolbartitle2;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener {

    LinearLayout motivation, spdm, gk, puzzle, tofw, reminder, wordpower;
    ImageButton ibtnmotivation, ibtnspdm, ibtngk, ibtnpuzzle, ibtntofw, ibtnreminder, ibtnwordpower;

//    RelativeLayout motivation;

    ScrollView scrollView;
    Animation slideUp, slideDown;
//    RelativeLayout reminder;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(30, 16, 30, 0);
        appBarLayout.setLayoutParams(layoutParams);
        toolbartitle2.setVisibility(View.GONE);
        toolbartitle.setVisibility(View.VISIBLE);
        toolbartitle.setText("Winspire Go");
        toolbartitle.setTextColor(getResources().getColor(R.color.Black));
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

        ibtnmotivation = view.findViewById(R.id.ibtnMotivation);
        ibtnspdm = view.findViewById(R.id.ibtnSpdm);
        ibtngk = view.findViewById(R.id.ibtnGk);
        ibtnpuzzle = view.findViewById(R.id.ibtnPuzzle);
        ibtntofw = view.findViewById(R.id.ibtnTopic);
        ibtnreminder = view.findViewById(R.id.ibtnTodo);
        ibtnwordpower = view.findViewById(R.id.ibtnWp);

        motivation.setOnClickListener(this);
        spdm.setOnClickListener(this);
        gk.setOnClickListener(this);
        puzzle.setOnClickListener(this);
        tofw.setOnClickListener(this);
        reminder.setOnClickListener(this);
        wordpower.setOnClickListener(this);

        ibtnmotivation.setOnClickListener(this);
        ibtnspdm.setOnClickListener(this);
        ibtngk.setOnClickListener(this);
        ibtnpuzzle.setOnClickListener(this);
        ibtntofw.setOnClickListener(this);
        ibtnreminder.setOnClickListener(this);
        ibtnwordpower.setOnClickListener(this);

        slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        slideUp.setDuration(500);
        slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slideDown.setDuration(500);
        scrollView = view.findViewById(R.id.catScrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP && scrollView != null) {
                    reminder.setVisibility(View.VISIBLE);
                    reminder.startAnimation(slideUp);
                }else {
                    reminder.setVisibility(View.GONE);
                    reminder.startAnimation(slideUp);
                }
                return false;
            }
        });

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            //scroll view is at bottom
                            reminder.setVisibility(View.GONE);
                            reminder.startAnimation(slideDown);
                        } else {
                            //scroll view is not at bottom
                            reminder.setVisibility(View.VISIBLE);
                            reminder.startAnimation(slideUp);
                        }
                    }
                });

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

            case R.id.ibtnMotivation:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new MotivationFragment()).commit();
                }
                break;

            case R.id.ibtnSpdm:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new SpeedMathsFragment()).commit();
                }
                break;

            case R.id.ibtnGk:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new GKFragment()).commit();
                }
                break;

            case R.id.ibtnPuzzle:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new PuzzleFragment()).commit();
                }
                break;
            case R.id.ibtnTodo:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new ReminderFragment()).commit();
                }
                break;
            case R.id.ibtnTopic:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new TopicFragment()).commit();
                }
                break;
            case R.id.ibtnWp:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, new WordPowerFragment()).commit();
                }
                break;
        }
    }

}
