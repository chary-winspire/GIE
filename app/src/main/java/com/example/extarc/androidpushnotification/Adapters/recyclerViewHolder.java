package com.example.extarc.androidpushnotification.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.extarc.androidpushnotification.R;

public class recyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "recyclerViewHolder";
    public TextView title;
    public TextView client;
    public TextView location;
    public ImageView logo;
    public RelativeLayout jobListItem;
    public Context context;
    public TextView lastDate;
    private RecyclerViewOnClickListener.OnClickListener onClickListener;
    public recyclerViewHolder(View view) {
        super(view);

        this. title = (TextView) itemView.findViewById(R.id.questionSpdm);
        this. client = (TextView) itemView.findViewById(R.id.ansSpdm);

      //  this.jobListItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // setting custom listener
        if (onClickListener != null) {
            onClickListener.OnItemClick(v, getAdapterPosition());

        }

    }

    // Setter for listener
    public void setClickListener(
            RecyclerViewOnClickListener.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }}

