package com.kokutouda.dnote.dnote.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class NotesListOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mClickListener;
    private GestureDetector mGestureDetector;

    public NotesListOnItemTouchListener(Context context, OnItemClickListener onItemClickListener) {
        mClickListener = onItemClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View itemView = rv.findChildViewUnder(e.getX(), e.getY());
        if (mClickListener != null && itemView != null && mGestureDetector.onTouchEvent(e)) {
            int position = rv.getChildAdapterPosition(itemView);
            mClickListener.onItemClick(itemView, position);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
