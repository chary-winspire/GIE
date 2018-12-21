package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class WordPowerFragment extends Fragment {

    TextView descText;
    ImageButton plus, minus;

    private final String TAG = "WordPowerFragment";


    ExpandableRelativeLayout expandableLayout;
    static Button wpTitle;

    private List<WordPowerModel> WordsList;
    WordPowerModel wordPowerModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private WordPowerAdapter WPAdapter;
    int ID=1;
    public SharedPreferences preferences;

    RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();
    private String questionnaireListStr = null;


    public WordPowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.fragment_word_power, container, false);

        ArrayList<Questionnaire> Wordslist = new ArrayList<>();
        recyclerView = (RecyclerView) myview.findViewById(R.id.wpRecylerView);

        wpTitle = myview.findViewById(R.id.wpTitle);
        wpTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout = (ExpandableRelativeLayout) myview.findViewById(R.id.expandableLayout);
                if (expandableLayout.isExpanded()) {
                    wpTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
                    expandableLayout.collapse();
//                    expandableLayout1.toggle(); // toggle expand and collapse
                } else {
                    wpTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropup, 0);
                    expandableLayout.expand();
                }
            }
        });
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID= Integer.valueOf(preferences.getString(Constants.WORD_POWER,"1"));




        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(layoutParams);
        toolbar.setBackgroundColor(getResources().getColor(R.color.QuoraRed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        toolbartitle.setVisibility(View.GONE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("Word Coach");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));
        toolbar.setNavigationIcon(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

//        plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plus.setVisibility(View.GONE);
//                minus.setVisibility(View.VISIBLE);
//                descText.setMaxLines(Integer.MAX_VALUE);
//            }
//        });
//        minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                minus.setVisibility(View.GONE);
//                plus.setVisibility(View.VISIBLE);
//                descText.setMaxLines(1);
//            }
//        });
        getQuestionnaire(ID,"WORD");
        return myview;
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

                    WPAdapter = new WordPowerAdapter(arryJobDetails);
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(WPAdapter);

                }
            } else {
                Log.i(TAG, " List completed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class WordPowerAdapter extends RecyclerView.Adapter<WordPowerAdapter.MyViewHolder> {

        private ArrayList<Questionnaire> WordsList;

        public WordPowerAdapter(ArrayList<Questionnaire> wordsList) {
            this.WordsList = wordsList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView type, meaning, example;
            public Button title;
            ExpandableRelativeLayout expandableLayout1;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.wpTitle);
                type = view.findViewById(R.id.wpType);
                meaning = view.findViewById(R.id.wpMeaning);
                example = view.findViewById(R.id.wpExample);
                expandableLayout1 = view.findViewById(R.id.expandableLayout);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wordpowerview, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
//            WordPowerModel movie = WordsList.get(position);
            holder.title.setText(WordsList.get(position).getQuestion());
            holder.type.setText(WordsList.get(position).getUserAnswer());
            holder.meaning.setText(WordsList.get(position).getOption1());
            holder.example.setText(WordsList.get(position).getComment());
            holder.expandableLayout1.collapse();

            Log.d(TAG, "WPContent" + WordsList);

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                    if (holder.expandableLayout1.isExpanded()) {
                        holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
                        holder.expandableLayout1.collapse();
//                        expandableLayout1.toggle(); // toggle expand and collapse
                    } else {
                        holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropup, 0);
                        holder.expandableLayout1.expand();
                    }
                }
            });

//            holder.title.setText(movie.getWPTitle());
//            holder.type.setText(movie.getWPType());
//            holder.meaning.setText(movie.getWPMening());
//            holder.example.setText(movie.getWPExample());
        }

        @Override
        public int getItemCount() {
            if (WordsList != null) {
                return WordsList.size();
            } else {
                return 0;
            }
        }
    }
}
