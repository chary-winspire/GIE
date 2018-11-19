package com.example.extarc.androidpushnotification.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.extarc.androidpushnotification.Models.Questionnaire;
import com.example.extarc.androidpushnotification.R;

import java.util.ArrayList;

public class CustomSpdMAdapter extends  RecyclerView.Adapter<recyclerViewHolder> {

    private static final String TAG = "ListViewRecyclerAdapter";
    // recyclerview adapter
    private ArrayList<Questionnaire> arrayList;
    private Context context;
    ArrayList<Bitmap> arryBitmaps = new ArrayList<Bitmap>();

    public CustomSpdMAdapter(Context context,
                                   ArrayList<Questionnaire> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(recyclerViewHolder holder, int position) {
        final Questionnaire model = arrayList.get(position);
        recyclerViewHolder mainHolder = (recyclerViewHolder) holder;// holder
        final Questionnaire objJobDetails = arrayList.get(position);

            mainHolder.title.setText(objJobDetails.getQuestion());

        mainHolder.client.setText(objJobDetails.getUserAnswer());


        // Implement click listener over layout
        mainHolder.setClickListener(new RecyclerViewOnClickListener.OnClickListener() {

            @Override
            public void OnItemClick(View view, int position) {

            }

        });
    }



    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.speedmaths_view, viewGroup, false);
        recyclerViewHolder listHolder = new recyclerViewHolder(mainGroup);

        return listHolder;

    }



}
