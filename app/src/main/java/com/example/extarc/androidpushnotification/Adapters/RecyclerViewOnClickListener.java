package com.example.extarc.androidpushnotification.Adapters;

import android.view.View;

public class RecyclerViewOnClickListener {
    /**
     * Interface for Item Click over Recycler View Items
     **/
    public interface OnClickListener {
        public void OnItemClick(View view, int position);
    }
}
