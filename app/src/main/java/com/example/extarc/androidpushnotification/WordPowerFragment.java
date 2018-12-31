package com.example.extarc.androidpushnotification;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
import static com.example.extarc.androidpushnotification.MasterActivity.bottomNavigation;
import static com.example.extarc.androidpushnotification.MasterActivity.directory;
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
    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
    String imageName = "WordCoach";
    File imagePath = new File(directory + "/" + imageName + timeStamp + ".jpeg");


    ExpandableRelativeLayout expandableLayout;
//    static Button wpTitle;

    private List<WordPowerModel> WordsList;
    WordPowerModel wordPowerModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private WordPowerAdapter WPAdapter;
    int ID = 1;
    public SharedPreferences preferences;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Questionnaire> arryJobDetails = new ArrayList<>();
    private String questionnaireListStr = null;

    int count = 0;
    int wordNo = 1;
    int learneInt = 0;
    int knownInt = 0;
    ImageButton nextWord, prevWord;
    Button wpTitle;
    TextView wpType, wpExample;
    TextView meaning1, meaning2, meaning3, meaning4;
    Button learnedWord, iknowWord;
    TextView learned, known, wordno;
    Button wordsList;
    RelativeLayout WordsListFull;

    ImageButton shareWP;
    ImageView logo_shareWp;

    public WordPowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.fragment_word_power, container, false);

        recyclerView = (RecyclerView) myview.findViewById(R.id.wpRecylerView);

        shareWP = myview.findViewById(R.id.shareWP);
        logo_shareWp = myview.findViewById(R.id.logo_shareWP);
        logo_shareWp.setVisibility(View.GONE);
        shareWP.setVisibility(View.VISIBLE);
        shareWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_shareWp.setVisibility(View.VISIBLE);
                shareWP.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = takeScreenshot();
                        saveBitmap(bitmap);
                        shareIt();
                        logo_shareWp.setVisibility(View.VISIBLE);
                        shareWP.setVisibility(View.GONE);
                    }
                }, 200);
            }
        });

//        wpTitle = myview.findViewById(R.id.wpTitle);
//        wpTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expandableLayout = (ExpandableRelativeLayout) myview.findViewById(R.id.expandableLayout);
//                if (expandableLayout.isExpanded()) {
//                    wpTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
//                    expandableLayout.collapse();
////                    expandableLayout1.toggle(); // toggle expand and collapse
//                } else {
//                    wpTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropup, 0);
//                    expandableLayout.expand();
//                }
//            }
//        });
        preferences = getActivity().getSharedPreferences(Constants.SP_PERSISTENT_VALUES,
                Context.MODE_PRIVATE);
        ID = Integer.valueOf(preferences.getString(Constants.WORD_POWER, "1"));

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        appBarLayout.setLayoutParams(layoutParams);
        toolbar.setBackgroundColor(getResources().getColor(R.color.QuoraRed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        toolbartitle.setVisibility(View.GONE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("Word Coach");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));
//        toolbar.setNavigationIcon(null);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

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

        wpTitle = myview.findViewById(R.id.wpTitle);
        wpType = myview.findViewById(R.id.wpType);
        wpExample = myview.findViewById(R.id.wpExample);
        meaning1 = myview.findViewById(R.id.wpMeaning1);
        meaning2 = myview.findViewById(R.id.wpMeaning2);
        meaning3 = myview.findViewById(R.id.wpMeaning3);
        meaning4 = myview.findViewById(R.id.wpMeaning4);

        nextWord = myview.findViewById(R.id.nextWord);
        prevWord = myview.findViewById(R.id.prevWord);

        learned = myview.findViewById(R.id.lerned);
        known = myview.findViewById(R.id.known);
        wordno = myview.findViewById(R.id.wordno);
        learnedWord = myview.findViewById(R.id.learnedWord);
        iknowWord = myview.findViewById(R.id.iKnowWord);
        wordNo = wordNo++;

        wordno.setText("5\nWords");
        known.setText("0/5\n" + "Known Words");
        learned.setText("0/5\n" + "Learned Words");

        wordsList = myview.findViewById(R.id.Wordlist);
        WordsListFull = myview.findViewById(R.id.WpfullView);
        wordsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.isShown()) {
                    recyclerView.setVisibility(View.GONE);
                    WordsListFull.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    WordsListFull.setVisibility(View.GONE);
                }
            }
        });

        nextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= 0) {
                    count = count + 1;
                    getWords();
                    CountControll();
                    wordNo = wordNo + 1;
                    wordno.setText("Word No.\n" + wordNo + "/5");
                    iknowWord.setVisibility(View.VISIBLE);
                    learnedWord.setVisibility(View.VISIBLE);
                }
            }
        });
        prevWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= 0) {
                    count = count - 1;
                    getWords();
                    CountControll();
                    wordNo = wordNo - 1;
                    wordno.setText("Word No.\n" + wordNo + "/5");
                }
            }
        });
        knownInt = knownInt++;
        learneInt = learneInt++;
        iknowWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knownInt = knownInt + 1;
                known.setText("Known Words\n" + knownInt);
                iknowWord.setVisibility(View.GONE);
                learnedWord.setVisibility(View.GONE);
            }
        });
        learnedWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learneInt = learneInt + 1;
                learned.setText("Learned Words\n" + learneInt);
                learnedWord.setVisibility(View.GONE);
                iknowWord.setVisibility(View.GONE);
            }
        });

        getQuestionnaire(6, "WORD");
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

                    getWords();
                    CountControll();
                    wordno.setText("Word No.\n" + "1/5");

                    WPAdapter = new WordPowerAdapter(arryJobDetails);
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(WPAdapter);
//                    recyclerView.setNestedScrollingEnabled(false);
//                    ViewCompat.setNestedScrollingEnabled(recyclerView, false);
//                    recyclerView.setLayoutFrozen(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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

            public TextView type, example;
            public TextView meaning1, meaning2, meaning3, meaning4;
            public Button title;
            ExpandableRelativeLayout expandableLayout1;
            Button wpLerned, wpIknow;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.wpTitle);
                type = view.findViewById(R.id.wpType);
                meaning1 = view.findViewById(R.id.wpMeaning1);
                meaning2 = view.findViewById(R.id.wpMeaning2);
                meaning3 = view.findViewById(R.id.wpMeaning3);
                meaning4 = view.findViewById(R.id.wpMeaning4);
                example = view.findViewById(R.id.wpExample);
                expandableLayout1 = view.findViewById(R.id.expandableLayout);
                wpLerned = view.findViewById(R.id.wplearned);
                wpIknow = view.findViewById(R.id.wpiknow);
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
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//            WordPowerModel movie = WordsList.get(position);
            holder.title.setText(WordsList.get(position).getQuestion());
            holder.type.setText(WordsList.get(position).getUserAnswer());
            holder.meaning1.setText(WordsList.get(position).getOption1());
            holder.meaning2.setText(WordsList.get(position).getOption2());
            holder.meaning3.setText(WordsList.get(position).getOption3());
            holder.meaning4.setText(WordsList.get(position).getOption4());
            holder.example.setText(WordsList.get(position).getComment());

            if (meaning3 != null) {
                meaning3.setVisibility(View.VISIBLE);
            } else if (meaning3.getText().equals("")) {
                meaning3.setVisibility(View.GONE);
            }
            if (meaning4 != null) {
                meaning4.setVisibility(View.VISIBLE);
            } else if (meaning4.getText().equals("")) {
                meaning4.setVisibility(View.GONE);
            }

            Log.d(TAG, "WPContent" + WordsList);

            final int Position = holder.getAdapterPosition();
            if (Position == 0) {
                if (holder.expandableLayout1.isExpanded()) {
                    holder.expandableLayout1.collapse();
                    holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
                } else {
                    holder.expandableLayout1.expand();
                    holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropup, 0);
                }
            } else {
                holder.expandableLayout1.collapse();
                holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
            }

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Collections.swap(arryJobDetails, Position, 0);
//                    notifyItemMoved(Position, 0);
//                    Log.d(TAG, "Recycler Position" + Position);

//                    Toast.makeText(getApplicationContext(), "Recycle Click" + Position, Toast.LENGTH_SHORT).show();
                    if (holder.expandableLayout1.isExpanded()) {
                        holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropdown, 0);
                        holder.expandableLayout1.collapse();
//                        expandableLayout1.toggle(); // toggle expand and collapse
                    } else {
                        holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_dropup, 0);
                        holder.expandableLayout1.expand();
//                        notifyItemChanged(0);
                    }
                }
            });

            holder.wpLerned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    learneInt = learneInt + 1;
                    learned.setText(learneInt + "/5\n" + "Learned Words");
                    holder.wpLerned.setAlpha(0.5f);
                    holder.wpIknow.setAlpha(0.5f);
                    holder.wpLerned.setClickable(false);
                    holder.wpIknow.setEnabled(false);
                }
            });

            holder.wpIknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    knownInt = knownInt + 1;
                    known.setText(knownInt + "/5\n" + "Known Words");
                    holder.wpLerned.setAlpha(0.5f);
                    holder.wpIknow.setAlpha(0.5f);
                    holder.wpLerned.setClickable(false);
                    holder.wpIknow.setEnabled(false);
                }
            });

//            holder.title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    int itemPosition = recyclerView.indexOfChild(v);
//                    Toast.makeText(getActivity(), "You Clicked" + position, Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        public void moveToTop(ArrayList<Questionnaire> WordsList, int pos) {
            if (pos >= 0) {
                WordsList.add(0, WordsList.remove(pos));
                notifyDataSetChanged();
            }
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

    private void getWords() {
        wpTitle.setText(arryJobDetails.get(count).getQuestion());
        wpType.setText(arryJobDetails.get(count).getUserAnswer());
        wpExample.setText(arryJobDetails.get(count).getComment());
        meaning1.setText(arryJobDetails.get(count).getOption1());
        meaning2.setText(arryJobDetails.get(count).getOption2());

        String Meaning3 = arryJobDetails.get(count).getOption3();
        String Meaning4 = arryJobDetails.get(count).getOption4();
        if (Meaning3 == null || Meaning3.matches("")) {
            meaning3.setVisibility(View.GONE);
        } else {
            meaning3.setVisibility(View.VISIBLE);
            meaning3.setText(arryJobDetails.get(count).getOption3());
        }
        if (Meaning4 == null || Meaning4.matches("")) {
            meaning4.setVisibility(View.GONE);
        } else {
            meaning4.setVisibility(View.VISIBLE);
            meaning4.setText(arryJobDetails.get(count).getOption4());
        }
    }

    public void CountControll() {
        if (count > 0) {
            prevWord.setVisibility(View.VISIBLE);
        } else if (count == 0) {
            prevWord.setVisibility(View.INVISIBLE);
        }
        if (count < 4) {
            nextWord.setVisibility(View.VISIBLE);
        } else if (count == 4) {
            nextWord.setVisibility(View.INVISIBLE);
        }
    }

    public void saveBitmap(Bitmap bitmap) {
//        imagePath = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getAbsolutePath() + "/" + imageName + "-" + dateStr + ".jpeg"); ////File imagePath
        FileOutputStream fos;
//        addImageWaterMark();
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        Toast.makeText(getApplicationContext(), "Image Saved at: " + imagePath, Toast.LENGTH_SHORT).show();
    }

    public Bitmap takeScreenshot() {
        final View v1 = getActivity().getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
//        ansLayPuzzle.setDrawingCacheEnabled(true);
//        return ansLayPuzzle.getDrawingCache();
        return v1.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
