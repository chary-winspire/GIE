package com.example.extarc.androidpushnotification;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.extarc.androidpushnotification.MasterActivity.appBarLayout;
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
public class TopicFragment extends Fragment {

    RecyclerView recyclerView;
    TopicAdapter topicAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        appBarLayout.setLayoutParams(layoutParams);
        toolbartitle.setVisibility(View.GONE);
        toolbartitle2.setVisibility(View.VISIBLE);
        toolbartitle2.setText("Top Reads");
        toolbartitle2.setTextColor(getResources().getColor(R.color.White));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Topic_Toolbar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.Black));
        }
        bottomNavigation.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        toolbar.setNavigationIcon(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigation_menu));

        ArrayList<WordPowerModel> TopicList = new ArrayList<>();

        TopicList.add(new WordPowerModel("http://winspiremagazine.com/articles/wp-content/uploads/2017/11/kAIZEN-CONTINIOUES-IMPROVEMENT-300x168.png",
                "Kaizen way of Learning: Work hard OR Work Smart!",
                "",
                "http://winspiremagazine.com/articles/index.php/2017/11/02/kaizen-way-learning-work-hard-work-smart/"));
        TopicList.add(new WordPowerModel("http://winspiremagazine.com/articles/wp-content/uploads/2017/11/How-to-Wake-Up-Early-Every-Day-300x203.png",
                "How to Wake Up Early Every Day? 5 Simple Steps",
                "",
                "http://winspiremagazine.com/articles/index.php/2017/11/02/wake-early-every-day-5-simple-steps/"));
        TopicList.add(new WordPowerModel("http://winspiremagazine.com/articles/wp-content/uploads/2017/11/personal-accountability-300x203.png",
                "How to Teach Kids the Value Personal Accountability",
                "",
                "http://winspiremagazine.com/articles/index.php/2017/11/02/teach-kids-value-personal-accountability/"));
        TopicList.add(new WordPowerModel("http://winspiremagazine.com/articles/wp-content/uploads/2017/11/focus-768x544.png",
                "How to Focus on Studying Without Distractions?",
                "",
                "http://winspiremagazine.com/articles/index.php/2017/11/02/focus-studying-without-distractions/"));
        TopicList.add(new WordPowerModel("http://winspiremagazine.com/articles/wp-content/uploads/2017/11/Cryptocurrency-300x203.jpg",
                "Cryptocurrency",
                "Cryptocurrency is a digital currency or internet money  for which encryption techniques are used to regulate its use and generate its release.",
                "http://winspiremagazine.com/articles/index.php/2017/11/06/topic-day-cryptocurrency-06112017/"));


        recyclerView = view.findViewById(R.id.TopicRecycler);
        topicAdapter = new TopicAdapter(TopicList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(topicAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        return view;
    }

    class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {

        private ArrayList<WordPowerModel> TopicList;

        public TopicAdapter(ArrayList<WordPowerModel> TopicList) {
            this.TopicList = TopicList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView Title, Des;
            public ImageView TopicImage;
            RelativeLayout TopicView;

            public MyViewHolder(View view) {
                super(view);
                Title = view.findViewById(R.id.TopicTitle);
                Des = view.findViewById(R.id.TopicDes);
                TopicImage = view.findViewById(R.id.TopicImage);
                TopicView = view.findViewById(R.id.Topiccardview);
            }
        }

        @NonNull
        @Override
        public TopicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topicview_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final TopicAdapter.MyViewHolder holder, final int position) {
            holder.Title.setText(TopicList.get(position).getTopicTitle());
            holder.Des.setText(TopicList.get(position).getTopicDes());
            String imageUrl = TopicList.get(position).getTopicImage();
            Glide.with(getContext()).load(imageUrl).into(holder.TopicImage);

            holder.TopicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TopicURL = TopicList.get(position).getTopicURL();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(TopicURL));
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (TopicList != null) {
                return TopicList.size();
            } else {
                return 0;
            }
        }
    }

}
